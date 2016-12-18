package com.liws.rpc.test.testnetty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liweisheng on 16/9/14.
 */
public class MessageSendHandler extends ChannelOutboundHandlerAdapter {
    private static Logger LOG = LoggerFactory.getLogger(MessageSendHandler.class);
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        Message m = (Message)msg;
        ByteBuf buf = ctx.alloc().buffer();
        byte[] msgAsBytes = m.getAsByteArray();
        int msgLength = msgAsBytes.length;
        buf.writeInt(msgLength);
        buf.writeBytes(msgAsBytes);
        ctx.write(buf, promise);
        LOG.debug("send msg:{}", m.getJsonContent());

        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                ReferenceCountUtil.safeRelease(buf);
            }
        });

        flush(ctx);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        super.flush(ctx);
    }

}
