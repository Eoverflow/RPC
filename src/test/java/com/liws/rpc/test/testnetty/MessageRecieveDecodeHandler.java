package com.liws.rpc.test.testnetty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Created by liweisheng on 16/9/13.
 */
public class MessageRecieveDecodeHandler extends ByteToMessageDecoder {
    private static Logger LOG = LoggerFactory.getLogger(MessageRecieveDecodeHandler.class);
    private int messageSize = -1;
    private int count =0;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        LOG.debug("recieve byte:{}",byteBuf.readableBytes());

        if(messageSize == -1){

            if(byteBuf.readableBytes() < 4){
                return;
            }

            messageSize = byteBuf.readInt();
        }

        if(byteBuf.readableBytes() < messageSize){
            return;
        }

        byte[] buf = new byte[messageSize];

        byteBuf.readBytes(buf, 0, messageSize);
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Message message = (Message)ois.readObject();
        LOG.debug("recieve message:{} from:{}",message,channelHandlerContext.channel().remoteAddress());
        list.add(message);
        messageSize = -1;
    }



}
