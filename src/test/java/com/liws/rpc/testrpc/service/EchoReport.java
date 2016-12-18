package com.liws.rpc.testrpc.service;

/**
 * Created by liweisheng on 16/9/22.
 */
public interface EchoReport {
    String echo(String msg);
    void print(String msg);
}
