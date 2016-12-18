package com.liws.rpc.test.testnetty.testregistry;

import com.liws.rpc.LocalServiceRegistryCenter;
import com.liws.rpc.ipc.LocalServiceInvoke;
import com.liws.rpc.ipc.ServiceInvoke;
import com.liws.rpc.proxy.RpcClientProxy;

/**
 * Created by liweisheng on 16/9/20.
 */
public class TestLocalRpcClient {
    private LocalServiceRegistryCenter localServiceRegistryCenter;
    private ServiceInvoke serviceInvoke;

//    @Before
    public void before(){
        localServiceRegistryCenter = LocalServiceRegistryCenter.getInstance();
        try {
            localServiceRegistryCenter.registService("introduceLiHua",IntroduceLihua.class);
            localServiceRegistryCenter.registService("introduceLiMing",IntroduceLiMing.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        serviceInvoke = new LocalServiceInvoke();
    }

//    @Test
    public void testLocalRpcClient(){
        IntroduceSelf introduceLiHua = (IntroduceSelf)RpcClientProxy.newInstance("introduceLiHua",IntroduceSelf.class,serviceInvoke);
        IntroduceSelf introduceLiMing = (IntroduceSelf)RpcClientProxy.newInstance("introduceLiMing",IntroduceSelf.class,serviceInvoke);

        String lihuaMsg = introduceLiHua.report();
        String limingMsg = introduceLiMing.report();

        System.out.println(lihuaMsg);
        System.out.println(limingMsg);

        introduceLiHua.print("Hello");

        introduceLiMing.print("Hello","call me");
    }
}
