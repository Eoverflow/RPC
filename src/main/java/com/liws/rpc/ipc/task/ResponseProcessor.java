package com.liws.rpc.ipc.task;

import com.liws.rpc.ipc.Response;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liweisheng on 16/9/21.
 */
public class ResponseProcessor implements Runnable{
    private BlockingQueue<Response> responseBlockingQueue;
//    private Map<UUID,>
    public ResponseProcessor(BlockingQueue responseBlockingQueue){
        this.responseBlockingQueue = responseBlockingQueue;
    }

    @Override
    public void run() {
        for(;;){

        }
    }
}
