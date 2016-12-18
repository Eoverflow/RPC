package com.liws.rpc.ipc.remote;

import com.liws.rpc.ipc.Request;
import com.liws.rpc.ipc.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liweisheng on 16/9/21.
 */
public class ResponseSendHandler extends ChannelOutboundHandlerAdapter {
    private static Logger LOG = LoggerFactory.getLogger(RequestSendHandler.class);
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Response response = (Response)msg;
        ByteBuf buf = ctx.alloc().buffer();
        byte[] responseAsBytes = response.getAsByteArray();
        int responseLength = responseAsBytes.length;

        buf.writeInt(responseLength);
        buf.writeBytes(responseAsBytes);
        ctx.write(buf, promise);

        LOG.info("send response:{} to host:{}",response.getRequestUUID(),ctx.channel().remoteAddress());
        LOG.debug("send response:{},details:{}",response.getRequestUUID(),response);

        flush(ctx);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        super.flush(ctx);
    }
}
