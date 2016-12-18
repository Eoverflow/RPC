package com.liws.rpc.ipc.remote;

import com.liws.rpc.ipc.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Created by liweisheng on 16/9/20.
 */
public class RequestReceiveDecodeHandler extends ByteToMessageDecoder{
        private static Logger LOG = LoggerFactory.getLogger(RequestReceiveDecodeHandler.class);
        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
//            if(messageSize == -1){
//
//                if(byteBuf.readableBytes() < 4){
//                    return;
//                }
//                messageSize = byteBuf.readInt();
//            }

            if(byteBuf.readableBytes() >= 4){
                Integer requestLength = byteBuf.getInt(byteBuf.readerIndex());

                if(byteBuf.readableBytes() < requestLength + 4){
                    return;
                }
            }else{
                return;
            }

            Integer messageSize = byteBuf.readInt();

            byte[] buf = new byte[messageSize];

            byteBuf.readBytes(buf, 0, messageSize);
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Request request = (Request)ois.readObject();

            LOG.info("receive request from:{},uuid:{}",channelHandlerContext.channel().remoteAddress(),request.getUuid());
            LOG.debug("request uuid:{},detail:{}",request.getUuid(),request);

            list.add(request);
            messageSize = -1;
        }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("new client:{} connected",ctx.channel().remoteAddress());
    }
}
