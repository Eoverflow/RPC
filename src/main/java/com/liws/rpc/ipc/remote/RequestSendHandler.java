package com.liws.rpc.ipc.remote;

import com.liws.rpc.ipc.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liweisheng on 16/9/20.
 */
public class RequestSendHandler extends ChannelOutboundHandlerAdapter {
    private static Logger LOG = LoggerFactory.getLogger(RequestSendHandler.class);
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Request request = (Request)msg;
        ByteBuf buf = ctx.alloc().buffer();
        byte[] requestAsBytes = request.getAsByteArray();
        int requestLength = requestAsBytes.length;

        LOG.info("send request:{} to remote:{},request for service:{}",
                request.getUuid(),ctx.channel().remoteAddress(),request.getMethodSignatureName());

        LOG.debug("request:{}, details:{}",request.getUuid(),request);

        buf.writeInt(requestLength);
        buf.writeBytes(requestAsBytes);
        ctx.write(buf, promise);

        flush(ctx);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        super.flush(ctx);
    }
}
