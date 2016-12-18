package com.liws.rpc;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by liweisheng on 16/11/3.
 */
public class ZkInitializer {
    private String connectionStr;

    private int connectionTimeOut;

    private int sessionTimeOut;

    public ZkInitializer(String connectionStr, int connectionTimeOut, int sessionTimeOut) {
        this.connectionStr = connectionStr;
        this.connectionTimeOut = connectionTimeOut;
        this.sessionTimeOut = sessionTimeOut;
    }

    public CuratorFramework getZkClient(){
        return CuratorFrameworkFactory.newClient(connectionStr, sessionTimeOut, connectionTimeOut, new ExponentialBackoffRetry(100,5));
    }

    public String getConnectionStr() {
        return connectionStr;
    }

    public void setConnectionStr(String connectionStr) {
        this.connectionStr = connectionStr;
    }

    public int getConnectionTimeOut() {
        return connectionTimeOut;
    }

    public void setConnectionTimeOut(int connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public int getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }
}
