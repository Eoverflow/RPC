package com.liws.rpc.test.testnetty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by liweisheng on 16/9/14.
 */
public class FeedBackRecieveHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message m = (Message)msg;
        System.out.println(m.getJsonContent());
    }
}
