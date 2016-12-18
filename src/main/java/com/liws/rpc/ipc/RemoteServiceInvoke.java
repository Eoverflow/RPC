package com.liws.rpc.ipc;

import com.liws.rpc.constant.RpcStatus;
import com.liws.rpc.ipc.task.RequestFuture;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by liweisheng on 16/9/20.
 */
public class RemoteServiceInvoke implements ServiceInvoke{
    private ChannelFuture channelFuture;
    private Map<UUID, RequestFuture> uuidRequestFutureMap;
    public RemoteServiceInvoke(Map uuidRequestFutureMap,ChannelFuture channelFuture){
        this.uuidRequestFutureMap = uuidRequestFutureMap;
        this.channelFuture = channelFuture;
    }

    @Override
    public Response invoke(Request request) {
        Channel ch = channelFuture.channel();
        Response response = null;
        if(!ch.isOpen()){
            response = Response.createResponse("",request.getUuid(),RpcStatus.CONNECTION_LOSS);
            return response;
        }

        RequestFuture<Response> requestFuture = new RequestFuture();
        uuidRequestFutureMap.put(request.getUuid(), requestFuture);

        ChannelFuture writeFuture = channelFuture.channel().pipeline().write(request);

        ch.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Response response = Response.createResponse(future.cause().toString(),
                        request.getUuid(),RpcStatus.CONNECTION_LOSS);

                synchronized (requestFuture){
                    requestFuture.setNX(response);
                    requestFuture.notify();
                }

            }
        });

        writeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    Response response = Response.createResponse(future.cause().toString(), request.getUuid(), RpcStatus.CONNECTION_LOSS);
                    synchronized (requestFuture) {
                        requestFuture.set(response);
                        requestFuture.notify();
                    }
                }
            }
        });

        try {
            response = requestFuture.get();
        } catch (InterruptedException e) {
            response = Response.errorReturn(request.getUuid(),RpcStatus.SERVER_EXCEPTION);
        } catch (ExecutionException e) {
            response = Response.errorReturn(request.getUuid(),RpcStatus.SERVER_EXCEPTION);
        }

        return response;

    }


}
