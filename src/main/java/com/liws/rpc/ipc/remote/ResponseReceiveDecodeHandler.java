package com.liws.rpc.ipc.remote;

import com.liws.rpc.ipc.Request;
import com.liws.rpc.ipc.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Created by liweisheng on 16/9/21.
 */
public class ResponseReceiveDecodeHandler extends ByteToMessageDecoder{
    private static Logger LOG = LoggerFactory.getLogger(ResponseReceiveDecodeHandler.class);
    private int messageSize = -1;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
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
        Response response = (Response)ois.readObject();

        LOG.info("receive response from:{},uuid:{}",channelHandlerContext.channel().remoteAddress(),response.getRequestUUID());
        LOG.debug("response uuid:{},detail:{}",response.getRequestUUID(),response);

        list.add(response);
        messageSize = -1;
    }
}
