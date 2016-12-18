package com.liws.rpc.ipc;

import com.liws.rpc.ipc.remote.RequestSendHandler;
import com.liws.rpc.ipc.remote.ResponsePreProcessHandler;
import com.liws.rpc.ipc.remote.ResponseReceiveDecodeHandler;
import com.liws.rpc.ipc.task.RequestFuture;
import com.liws.rpc.proxy.RpcClientProxy;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Created by liweisheng on 16/9/20.
 */
public class RpcClient {
    private static Logger LOG = LoggerFactory.getLogger(RpcClient.class);
    private Map<UUID, RequestFuture> requestFutureMap;
    private BlockingQueue<Response> responseBlockingQueue;
    private String remoteIP;
    private int remotePort;
    private Bootstrap bootstrap;
    private ChannelFuture channelFuture;
    private boolean isLocal;
    private ExecutorService responseWorker;

    public RpcClient(String remoteIP, int remotePort)throws Exception{
        this.remoteIP = remoteIP;
        this.remotePort = remotePort;
        this.requestFutureMap = new ConcurrentHashMap<>();
        this.responseBlockingQueue = new LinkedBlockingQueue<>();
        initBootstrap();
        this.responseWorker = Executors.newCachedThreadPool();
        responseWorker.execute(new ResponseDispatcher(false));
    }

    public RpcClient(int port) throws Exception{
        this("127.0.0.1",port);
    }

    public Object consumeService(Class<?> interfaceClass, String serviceName){
//        ServiceInvoke serviceInvoke = new LocalServiceInvoke();
        ServiceInvoke serviceInvoke = new RemoteServiceInvoke(requestFutureMap,channelFuture);
        Object proxy = RpcClientProxy.newInstance(serviceName, interfaceClass, serviceInvoke);
        return proxy;
    }

    private void initBootstrap() throws Exception{
        EventLoopGroup clientWorkerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(clientWorkerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new RequestSendHandler()).addLast(new ResponseReceiveDecodeHandler())
                        .addLast(new ResponsePreProcessHandler(responseBlockingQueue));
            }
        });

        channelFuture = bootstrap.connect(this.remoteIP, this.remotePort).sync();

    }


    public String getRemoteIP() {
        return remoteIP;
    }

    public void setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public void putRequestFuture(UUID uuid, RequestFuture requestFuture){
        this.requestFutureMap.put(uuid,requestFuture);
    }

    public RequestFuture getRequestFuture(UUID uuid){
        return this.requestFutureMap.get(uuid);
    }

    public Map<UUID, RequestFuture> getRequestFutureMap() {
        return requestFutureMap;
    }

    public void setRequestFutureMap(Map<UUID, RequestFuture> requestFutureMap) {
        this.requestFutureMap = requestFutureMap;
    }

    public BlockingQueue<Response> getResponseBlockingQueue() {
        return responseBlockingQueue;
    }

    public void setResponseBlockingQueue(BlockingQueue<Response> responseBlockingQueue) {
        this.responseBlockingQueue = responseBlockingQueue;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public ExecutorService getResponseWorker() {
        return responseWorker;
    }

    public void setResponseWorker(ExecutorService responseWorker) {
        this.responseWorker = responseWorker;
    }

    class ResponseDispatcher implements Runnable{
        private boolean isShuttedDown;
        public ResponseDispatcher(boolean isShuttedDown){
            this.isShuttedDown = isShuttedDown;
        }
        public boolean isShuttedDown() {
            return isShuttedDown;
        }

        public void setIsShuttedDown(boolean isShuttedDown) {
            this.isShuttedDown = isShuttedDown;
        }

        @Override
        public void run() {
            LOG.info("ResponseDispatcher started");
            for(;!isShuttedDown;){
                try {
                    Response response = responseBlockingQueue.take();
                    if(null == response){
                        continue;
                    }
                    LOG.info("take a reponse:{}",response.getRequestUUID());
                    LOG.debug("response:{}, details:{}", response.getRequestUUID(), response);

                    RequestFuture requestFuture = requestFutureMap.get(response.getRequestUUID());

                    synchronized (requestFuture){
                        requestFuture.setNX(response);
                        requestFuture.notify();
                    }

                } catch (InterruptedException e) {
                    LOG.error(e.toString());
                }
            }

            LOG.info("ResponseDispatcher shutted down");
        }
    }
}
