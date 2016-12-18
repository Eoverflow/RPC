package com.liws.rpc.test.testnetty;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * Created by liweisheng on 16/9/13.
 */
public class ClientTest {
    public static void main(String args[]){
        String host = args[0];
        Integer port = Integer.parseInt(args[1]);
        RpcClient rpcClient = null;
        try {
            rpcClient = new RpcClient(host,port);
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }



        for(int i=0;i<100;++i){
            Message m = new Message(i);
            rpcClient.putMessage(m);

        }
//        rpcClient.close();

    }


}
