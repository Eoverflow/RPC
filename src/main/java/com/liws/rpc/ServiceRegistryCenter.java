package com.liws.rpc;

import com.liws.rpc.ipc.RpcServer;

/**
 * Created by liweisheng on 16/11/3.
 */
public interface ServiceRegistryCenter {
    public void registService(String serviceName, Class<?> implClass) throws Exception;

    public ServiceRegistry getService(String serviceName);
}
