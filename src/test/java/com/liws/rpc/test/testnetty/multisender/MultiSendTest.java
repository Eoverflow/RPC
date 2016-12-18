package com.liws.rpc.test.testnetty.multisender;

import com.liws.rpc.test.testnetty.Message;
import com.liws.rpc.test.testnetty.RpcClient;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by liweisheng on 16/9/14.
 */
class Sender implements Runnable{
    private RpcClient rpcClient;
    public Sender(RpcClient client){
        this.rpcClient = client;
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        for(int i=0 ;i<100;++i){
            Message m = new Message(thread.getId());
            rpcClient.putMessage(m);
        }
    }
}


public class MultiSendTest {
    private RpcClient rpcClient;

    @Before
    public void before(){
        try {
            rpcClient = new RpcClient("127.0.0.1",8880);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMultiSender(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i=0;i<10;++i) {
            executorService.execute(new Sender(rpcClient));
        }


        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
