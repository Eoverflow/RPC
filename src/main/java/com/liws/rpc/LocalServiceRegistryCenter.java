package com.liws.rpc;

import com.liws.rpc.ipc.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liweisheng on 16/9/20.
 */
public class LocalServiceRegistryCenter implements ServiceRegistryCenter{
    private static Logger LOG = LoggerFactory.getLogger(LocalServiceRegistryCenter.class);

    private Map<String, ServiceRegistry> cachedRegistries;

    private LocalServiceRegistryCenter(){
        cachedRegistries = new HashMap<>();
    }

    synchronized public void registService(String serviceName, Class<?> implClass) throws Exception{
        ServiceRegistry serviceRegistry = new ServiceRegistry(implClass,serviceName);

        LOG.info("regist new service:{}, class:{}",serviceName,implClass.getCanonicalName());

        cachedRegistries.put(serviceName, serviceRegistry);
    }

    synchronized public ServiceRegistry getService(String serviceName){
        return cachedRegistries.get(serviceName);
    }

    static class ServiceRegistryCenterHolder{
        private static LocalServiceRegistryCenter registryCenter = new LocalServiceRegistryCenter();
    }

    public static LocalServiceRegistryCenter getInstance(){
        return ServiceRegistryCenterHolder.registryCenter;
    }

}
