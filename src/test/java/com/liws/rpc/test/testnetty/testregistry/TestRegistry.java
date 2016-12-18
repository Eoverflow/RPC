package com.liws.rpc.test.testnetty.testregistry;

import com.alibaba.fastjson.JSON;
import com.liws.rpc.ServiceRegistry;
import com.liws.rpc.utils.CommonUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by liweisheng on 16/9/19.
 */
public class TestRegistry {
    private ServiceRegistry serviceRegistry;
    @Before
    public void before(){
        try {
            serviceRegistry = new ServiceRegistry(IntroduceLihua.class,"introduceLiHua");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegistry() throws Exception{
        if(null == serviceRegistry){
            return;
        }

        Class intrClazz = IntroduceSelf.class;

        serviceRegistry.invokeMethod("report",intrClazz.getCanonicalName(),null);

        String printMethodName = CommonUtils.getMethodSignatureName(intrClazz.getDeclaredMethod("print",String.class));

        serviceRegistry.invokeMethod(printMethodName,intrClazz.getCanonicalName(), JSON.toJSONString("Hello"));
    }
}
