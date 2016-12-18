package com.liws.rpc.ipc.remote;

import com.liws.rpc.ipc.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * Created by liweisheng on 16/9/20.
 */

/**
 * 收到新的request,将(request,request来源channel)包装作为一个处理单元(ProcessUnit)
 * 放到阻塞队列
 *
 * */
public class RequestPreProcessHandler extends ChannelInboundHandlerAdapter {
    private static Logger LOG = LoggerFactory.getLogger(RequestPreProcessHandler.class);
    private BlockingQueue<ProcessUnit> processUnitBlockingQueue;

    public RequestPreProcessHandler(BlockingQueue blockingQueue){
        this.processUnitBlockingQueue = blockingQueue;
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request)msg;
        ProcessUnit processUnit = new ProcessUnit(ctx.channel(),request);
        //TODO:放入阻塞队列

        LOG.debug("receive new request:{},wrap in ProcessUnit",request.getUuid());
        processUnitBlockingQueue.put(processUnit);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }
}
