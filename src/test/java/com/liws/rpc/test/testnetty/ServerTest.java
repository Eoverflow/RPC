package com.liws.rpc.test.testnetty;

import java.util.concurrent.TimeUnit;

/**
 * Created by liweisheng on 16/9/13.
 */
public class ServerTest {
    public static void main(String args[]){
        int port = Integer.parseInt(args[0]);

        RpcServer server = new RpcServer(port);
//        server.sync();

    }
}
