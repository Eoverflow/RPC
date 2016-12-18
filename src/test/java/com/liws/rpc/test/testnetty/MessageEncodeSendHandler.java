package com.liws.rpc.test.testnetty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liweisheng on 16/9/13.
 */
public class MessageEncodeSendHandler extends MessageToByteEncoder<Message>{

    private static Logger LOG = LoggerFactory.getLogger(MessageEncodeSendHandler.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        byte[] msgAsBytes = msg.getAsByteArray();
        int msgSize = msgAsBytes.length;

        out.writeInt(msgSize);
        out.writeBytes(msgAsBytes);

        LOG.debug("send msg:{}", msg);

        flush(ctx);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        super.flush(ctx);
    }
}
