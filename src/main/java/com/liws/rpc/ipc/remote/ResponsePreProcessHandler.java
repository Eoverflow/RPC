package com.liws.rpc.ipc.remote;

import com.liws.rpc.ipc.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * Created by liweisheng on 16/9/21.
 */
public class ResponsePreProcessHandler extends ChannelInboundHandlerAdapter {
    private static Logger LOG = LoggerFactory.getLogger(ResponsePreProcessHandler.class);

    private BlockingQueue<Response> responseBlockingQueue;

    public ResponsePreProcessHandler(BlockingQueue responseBlockingQueue){
        this.responseBlockingQueue = responseBlockingQueue;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = (Response)msg;

        LOG.info("get a response:{} from remote:{}",response.getRequestUUID(),ctx.channel().remoteAddress());
        LOG.debug("response:{},detail:{}",response.getRequestUUID(),response);

        responseBlockingQueue.put(response);
    }
}
