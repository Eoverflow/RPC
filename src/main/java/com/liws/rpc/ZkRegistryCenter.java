package com.liws.rpc;

import com.liws.rpc.ipc.RpcServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liweisheng on 16/11/3.
 */
public class ZkRegistryCenter implements ServiceRegistryCenter{
    private static Logger LOG = LoggerFactory.getLogger(ZkRegistryCenter.class);

    private ServiceRegistryCenter serviceRegistryCenter;

    private CuratorFramework zkClient;

    private RpcServer rpcServer;

    private ZkInitializer zkInitializer;

    private byte[] localHostPort;

    public ZkRegistryCenter(RpcServer rpcServer, ZkInitializer zkInitializer) throws Exception{
        this.serviceRegistryCenter = LocalServiceRegistryCenter.getInstance();
        this.rpcServer = rpcServer;
        this.zkInitializer = zkInitializer;
        this.zkClient = zkInitializer.getZkClient();
        this.localHostPort = rpcServer.getHostPort().getBytes();
        this.init();
    }

    private void init() throws Exception{
        zkClient.start();
        try {
            zkClient.create().creatingParentContainersIfNeeded()
                    .withProtection().withMode(CreateMode.PERSISTENT)
                    .forPath("/rpcServiceCenter");
        } catch (Exception e) {
            LOG.error("create registry center error,path:{}","/rpcServiceCenter");
            throw e;
        }
    }

    private void registToZk(String serviceName, String version){
        if(StringUtils.isEmpty(version)){
            version = "1.0";
        }

        StringBuilder servicePath = new StringBuilder();
        servicePath.append("/rpcServiceCenter/")
                .append(serviceName)
                .append("/")
                .append(version);

        try {
            zkClient.create().creatingParentContainersIfNeeded()
                    .withProtection()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(servicePath.toString(),localHostPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getHostPort(){
        return localHostPort;
    }

    @Override
    public void registService(String serviceName, Class<?> implClass) throws Exception {

    }

    @Override
    public ServiceRegistry getService(String serviceName) {
        return null;
    }
}
