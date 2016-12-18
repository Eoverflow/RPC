package com.liws.rpc.ipc.remote;

import com.liws.rpc.ipc.Request;
import io.netty.channel.Channel;
/**
 * Created by liweisheng on 16/9/20.
 */

public class ProcessUnit {
    private Channel requestChannel;
    private Request request;

    public ProcessUnit(Channel requestChannel, Request request) {
        this.requestChannel = requestChannel;
        this.request = request;
    }

    public Channel getRequestChannel() {
        return requestChannel;
    }

    public void setRequestChannel(Channel requestChannel) {
        this.requestChannel = requestChannel;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
