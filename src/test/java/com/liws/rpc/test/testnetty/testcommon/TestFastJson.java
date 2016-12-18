package com.liws.rpc.test.testnetty.testcommon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liws.rpc.ipc.Request;
import com.liws.rpc.utils.FastJsonUtil;
import org.junit.Test;

/**
 * Created by liweisheng on 16/11/10.
 */
public class TestFastJson {
    @Test
    public void testSerialize(){
        Request request = new Request("h","l","o");
        Request request2 = new Request("h","l","o");

        request.addArg(request2);
        request.addArg(new String("string"));
        request.addArg(1L);
        byte[] bytes = FastJsonUtil.toJsonBytes(request);
        Request request1 = JSON.parseObject(bytes, Request.class);
        JSONObject arg0 = (JSONObject)request1.getArg(0);


        System.out.println(JSON.toJavaObject(arg0,Request.class)
        );
    }
}
