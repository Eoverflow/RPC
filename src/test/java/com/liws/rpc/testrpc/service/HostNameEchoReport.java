package com.liws.rpc.testrpc.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by liweisheng on 16/9/22.
 */
public class HostNameEchoReport implements EchoReport {
    @Override
    public String echo(String msg) {
        String hostName = "";
        try {
            InetAddress localHostAddr = InetAddress.getLocalHost();
            hostName = localHostAddr.getHostName();
        } catch (UnknownHostException e) {
            hostName = "unkown host name";
        }

        return hostName + ":" + msg;
    }

    @Override
    public void print(String msg) {
        System.out.println("receive msg: " + msg);
    }
}
