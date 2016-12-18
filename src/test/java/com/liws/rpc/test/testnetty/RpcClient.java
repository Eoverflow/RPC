package com.liws.rpc.test.testnetty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liweisheng on 16/9/13.
 */
public class RpcClient {
    private Logger LOG = LoggerFactory.getLogger(RpcClient.class);
    private ChannelFuture channelFuture;
    private String host;
    private int port;
    private LinkedBlockingQueue<Message> messageQueue;
    private EventLoopGroup clientWorkerGroup;

    public RpcClient(String host,int port) throws Exception{
        this.host = host;
        this.port = port;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.clientWorkerGroup = new NioEventLoopGroup();
        this.channelFuture = startClient(host,port);

        channelFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("--------channel closed");
            }
        });

        startSender((NioSocketChannel) channelFuture.channel());
    }

    private ChannelFuture startClient(String host, int port) throws Exception{
        ChannelFuture f = null;

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(clientWorkerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new MessageSendHandler()).addLast(new MessageRecieveDecodeHandler()).addLast(new FeedBackRecieveHandler());
                }
            });
            f = bootstrap.connect(host, port).sync();

            LOG.info("rpc client start, connect to host:{},port:{} {}",host,port,f.channel().isOpen());

            return f;

    }

    public void close(){
        Channel ch = channelFuture.channel();
        try {
            ch.close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void putMessage(Message m){
        try {
            messageQueue.put(m);
            LOG.debug("put a message into blocking queue:{}",m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void startSender(NioSocketChannel ch) {
        Thread sendThread = new Thread(new SendWorker(ch,messageQueue));
//        sendThread.setDaemon(true);
        sendThread.start();
    }
}
