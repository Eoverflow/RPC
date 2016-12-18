package com.liws.rpc.ipc.task;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import com.liws.rpc.ServiceRegistry;
import com.liws.rpc.LocalServiceRegistryCenter;
import com.liws.rpc.constant.RpcStatus;
import com.liws.rpc.ipc.Request;
import com.liws.rpc.ipc.Response;
import com.liws.rpc.ipc.remote.ProcessUnit;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liweisheng on 16/9/21.
 */
public class RequestProcessor implements Runnable{
    private static Logger LOG = LoggerFactory.getLogger(RequestProcessor.class);

    private BlockingQueue<ProcessUnit> requestBlockingQueue;


    public RequestProcessor(BlockingQueue requestBlockingQueue){
        this.requestBlockingQueue = requestBlockingQueue;
    }

    @Override
    public void run() {
        LocalServiceRegistryCenter localServiceRegistryCenter = LocalServiceRegistryCenter.getInstance();
        for(;;){
            try {
                ProcessUnit processUnit = requestBlockingQueue.take();
                Channel ch = processUnit.getRequestChannel();
                Request request = processUnit.getRequest();



                UUID requestUuid = request.getUuid();
                String serviceName = request.getServiceName();
                String methodSignatureName = request.getMethodSignatureName();
                String interfaceCanonicalName = request.getInterfaceCanonicalName();
                String argsAsArray[] = (String[])request.getArgsJSONAsArray();

                LOG.info("get request from remote host:{},serviceName:{},methodName",
                        ch.remoteAddress(),serviceName,methodSignatureName);

                ServiceRegistry serviceRegistry = localServiceRegistryCenter.getService(serviceName);
                Response response = null;
                if(null == serviceRegistry){
                    //TODO:服务不支持
                }

                Long start = System.currentTimeMillis();
                try {
                    Object ret = serviceRegistry.invokeMethod(methodSignatureName, interfaceCanonicalName, argsAsArray);
                    response = Response.successReturn(ret,requestUuid);
                } catch (Exception e) {
                    LOG.error("error happened while processing rpc request,request body:{},remote host:{},exception:{}",
                            request,ch.remoteAddress(),e);
                    response = Response.createResponse(e.toString(), requestUuid, RpcStatus.SERVER_EXCEPTION);

                }
                StringBuilder absoluteServiceName = new StringBuilder();
                absoluteServiceName.append(serviceName).append("%").append(interfaceCanonicalName)
                        .append("%").append(methodSignatureName);

                LOG.info("service invoke:{},time cost:{}",absoluteServiceName.toString(),System.currentTimeMillis()-start);
                LOG.debug("send response to remote host:{},response body:{}",
                        ch.remoteAddress(),response);
                ch.write(response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
