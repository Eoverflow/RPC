package com.liws.rpc.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.liws.rpc.ipc.Request;

/**
 * Created by liweisheng on 16/11/10.
 */
public class FastJsonUtil {
    private static byte[] EMPTY = new byte[0];
    private static String UTF8 = "UTF-8";
    public static byte[] toJsonBytes(Object object,SerializerFeature ...features){
        byte[] bytes = EMPTY;

        SerializeWriter out = null;

        try {
            out = new SerializeWriter();
            JSONSerializer serializer = new JSONSerializer(out);
            for(SerializerFeature feature : features){
                serializer.config(feature,true);
            }

            serializer.write(object);
            bytes = out.toBytes(UTF8);
        } finally {
            if(null != out){
                out.close();
            }
        }

        return bytes;
    }
}
