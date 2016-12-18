package com.liws.rpc.ipc;

import com.liws.rpc.ServiceRegistry;
import com.liws.rpc.LocalServiceRegistryCenter;
import com.liws.rpc.ipc.remote.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liweisheng on 16/9/22.
 */
public class RpcServer {
    private static Logger LOG = LoggerFactory.getLogger(RpcServer.class);
    private String localHost;
    private int listenPort;
    private BlockingQueue<ProcessUnit> processUnitBlockingQueue;
    private LocalServiceRegistryCenter serviceRegistryCenter;
    private ChannelFuture serverChannelFuture;
    private ServerBootstrap bootstrap;
    private EventLoopGroup bossLoopGroup;
    private EventLoopGroup workerLoopGroup;
    private ExecutorService requestWorker;

    public RpcServer(String localHost,int listenPort){
        this.localHost = localHost;
        this.listenPort = listenPort;
        this.processUnitBlockingQueue = new LinkedBlockingQueue<>();
        this.requestWorker = Executors.newCachedThreadPool();
        this.serviceRegistryCenter = LocalServiceRegistryCenter.getInstance();
        this.serverChannelFuture = startServer();
        requestWorker.submit(new RequestProcessor());


    }

    public RpcServer(int listenPort){
        this("127.0.0.1", listenPort);
    }

    public void provideService(String serviceName, Class<?> implClass) throws  Exception{
        serviceRegistryCenter.registService(serviceName, implClass);
    }

    public String getHostPort(){
        return localHost + ":" + listenPort;
    }

    private ChannelFuture startServer(){
        bootstrap = new ServerBootstrap();
        bossLoopGroup = new NioEventLoopGroup();
        workerLoopGroup = new NioEventLoopGroup();
        bootstrap.group(bossLoopGroup,workerLoopGroup).channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new RequestReceiveDecodeHandler())
                        .addLast(new RequestPreProcessHandler(processUnitBlockingQueue))
                        .addLast(new ResponseSendHandler());
            }
        });

        bootstrap.option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true);


        ChannelFuture f = null;
        try {
            f = bootstrap.bind(localHost,listenPort).sync();
        } catch (InterruptedException e) {
            LOG.error("bind host:{},port:{}",localHost,listenPort);
        }

        LOG.info("rpc server start listening on port:{} isOpen:{}", listenPort, f.channel().isOpen());
        return f;
    }


    class RequestProcessor implements Runnable{
        private boolean isShuttedDown;

        public RequestProcessor(){
            this.isShuttedDown = false;
        }

        public boolean isShuttedDown() {
            return isShuttedDown;
        }

        public void setIsShuttedDown(boolean isShuttedDown) {
            this.isShuttedDown = isShuttedDown;
        }

        @Override
        public void run() {
            LOG.info("RequestProcess started");
            for(;!isShuttedDown;){
                Response response = null;
                Request request = null;
                Channel ch = null;
                try {
                    ProcessUnit processUnit = processUnitBlockingQueue.take();
                    ch = processUnit.getRequestChannel();
                    request = processUnit.getRequest();

                    LOG.info("processing new request:{}",request.getUuid());

                    String serviceName = request.getServiceName();

                    ServiceRegistry serviceRegistry = serviceRegistryCenter.getService(serviceName);

                    Long start = System.currentTimeMillis();

                    String ret = serviceRegistry.invokeMethod(request);

                    LOG.info("process request:{}, time cost:{}",request.getUuid(),System.currentTimeMillis() - start);
                    response = Response.successReturn(ret,request.getUuid());

                } catch (InterruptedException e) {
                    LOG.error("error happend in RequestProcessor,msg:{}",e.toString());
                } catch (Exception e){
                    response = Response.exceptionReturn(e,request.getUuid());
                }

                if(null != ch && ch.isOpen() && null != response){
                    ch.pipeline().write(response);
                }
            }
        }
    }
}
