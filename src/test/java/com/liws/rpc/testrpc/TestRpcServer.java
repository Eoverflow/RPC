package com.liws.rpc.testrpc;

import com.liws.rpc.ipc.RpcServer;
import com.liws.rpc.testrpc.service.EchoReport;
import com.liws.rpc.testrpc.service.HostNameEchoReport;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by liweisheng on 16/9/22.
 */
public class TestRpcServer {
    private RpcServer rpcServer;

    @Before
    public void before(){
        rpcServer = new RpcServer(9000);
    }

    @Test
    public void testRpcServer(){
        try {
            rpcServer.provideService("hostNameEchoReport", HostNameEchoReport.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
