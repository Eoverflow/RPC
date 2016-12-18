package com.liws.rpc.test.testnetty;

import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.Channel;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liweisheng on 16/9/13.
 */
public class SendWorker implements Runnable {
    private static Logger LOG = LoggerFactory.getLogger(SendWorker.class);

    private NioSocketChannel bindedChannel;
    private LinkedBlockingQueue<Message> messageQueue;
    public SendWorker(NioSocketChannel channel,LinkedBlockingQueue messageQueue){
        this.bindedChannel = channel;
        this.messageQueue = messageQueue;
    }
    @Override
    public void run() {
        LOG.info("SendWorker running, send destination:{}",bindedChannel.remoteAddress());
        for(;;){
            try {
                Message m = messageQueue.take();
                LOG.debug("get a message:{}",m);
                bindedChannel.pipeline().write(m);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
