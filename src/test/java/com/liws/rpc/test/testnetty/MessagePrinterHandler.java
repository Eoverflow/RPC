package com.liws.rpc.test.testnetty;

import io.netty.channel.*;

/**
 * Created by liweisheng on 16/9/13.
 */
public class MessagePrinterHandler extends ChannelInboundHandlerAdapter{
    private Long count = 0L;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        Message m = (Message)msg;
//        System.out.println(m);m
        System.out.println("count: " + ++count);
        ctx.writeAndFlush(new Message("thank you"));
    }


}