package com.liws.rpc.testrpc;

import com.liws.rpc.ipc.LocalServiceInvoke;
import com.liws.rpc.ipc.RemoteServiceInvoke;
import com.liws.rpc.ipc.RpcClient;
import com.liws.rpc.ipc.ServiceInvoke;
import com.liws.rpc.proxy.RpcClientProxy;
import com.liws.rpc.testrpc.service.EchoReport;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by liweisheng on 16/9/22.
 */
public class TestRpcClient {
    private RpcClient rpcClient;
    @Before
    public void before(){
        try {
            rpcClient = new RpcClient(9000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        ServiceInvoke serviceInvoke = new RemoteServiceInvoke(rpcClient.getRequestFutureMap(),rpcClient.getChannelFuture());
        EchoReport proxy = (EchoReport)RpcClientProxy.newInstance("hostNameEchoReport", EchoReport.class, serviceInvoke);
        EchoReport proxy1 = (EchoReport)rpcClient.consumeService(EchoReport.class, "hostNameEchoReport");

        Long start = System.currentTimeMillis();
        String echo = proxy1.echo("Hello");
        Long end = System.currentTimeMillis();
        System.out.println("echo : " + echo + ", time cost:" + (end - start));
    }
}
