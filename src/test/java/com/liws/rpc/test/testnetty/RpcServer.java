package com.liws.rpc.test.testnetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.BindException;

/**
 * Created by liweisheng on 16/9/13.
 */
public class RpcServer {
    private static Logger LOG = LoggerFactory.getLogger(RpcServer.class);
    private int port;

    private ChannelFuture serverChannelFuture;
    private EventLoopGroup bossLoopGroup;
    private EventLoopGroup workerLoopGroup;

    public RpcServer(int port){
        this.port = port;
        this.serverChannelFuture = startServer();
        boolean b = serverChannelFuture.isSuccess();
    }

    private ChannelFuture startServer(){
        bossLoopGroup = new NioEventLoopGroup();
        workerLoopGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossLoopGroup,workerLoopGroup).channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new MessageRecieveDecodeHandler()).addLast(new MessagePrinterHandler()).
                        addFirst(new MessageSendHandler());
            }
        });

        bootstrap.option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true);

        try {
            ChannelFuture f = bootstrap.bind(port).sync();
            LOG.info("rpc server start listening on port:{} isOpen:{}", port, f.channel().isOpen());
//            f.channel().closeFuture().sync();
            return f;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void sync(){
        try {
            this.serverChannelFuture.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossLoopGroup.shutdownGracefully();
            workerLoopGroup.shutdownGracefully();
        }
    }
}
