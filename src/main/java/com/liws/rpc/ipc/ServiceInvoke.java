package com.liws.rpc.ipc;

import com.liws.rpc.ipc.task.RequestFuture;

/**
 * Created by liweisheng on 16/9/20.
 */
public interface ServiceInvoke {
    public Response invoke(Request request);
}
