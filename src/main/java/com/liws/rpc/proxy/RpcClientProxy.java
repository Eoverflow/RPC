package com.liws.rpc.proxy;

import com.alibaba.fastjson.JSON;
import com.liws.rpc.ipc.Request;
import com.liws.rpc.ipc.Response;
import com.liws.rpc.ipc.ServiceInvoke;
import com.liws.rpc.utils.CommonUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by liweisheng on 16/9/19.
 */
public class RpcClientProxy implements InvocationHandler {

    private String serviceName;
    private String interfaceCannonicalName;
    private Class<?> inerfaceClass;
    //TODO:增加发起请求的客户端
    private ServiceInvoke serviceInvoke;


    protected RpcClientProxy(String serviceName, Class interfaceClass,ServiceInvoke serviceInvoke){
        this.serviceName = serviceName;
        this.inerfaceClass = interfaceClass;
        this.interfaceCannonicalName = interfaceClass.getCanonicalName();
        this.serviceInvoke = serviceInvoke;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodSignatureName = CommonUtils.getMethodSignatureName(method);
        Request request = new Request(interfaceCannonicalName,serviceName,methodSignatureName);


        if(null != args){
            for(Object arg : args){
                request.addArg(arg);
            }
        }

        Response response = this.serviceInvoke.invoke(request);

        Class<?> returnTypeClass = method.getReturnType();

        String retJSON = response.getRet();

        return JSON.parseObject(retJSON,returnTypeClass);
    }

    public static Object newInstance(String serviceName,Class interfaceClass,ServiceInvoke serviceInvoke){

        InvocationHandler h = new RpcClientProxy(serviceName,interfaceClass,serviceInvoke);
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class[]{interfaceClass},h);
    }
}
