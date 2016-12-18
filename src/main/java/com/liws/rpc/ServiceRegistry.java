package com.liws.rpc;

import com.alibaba.fastjson.JSON;
import com.liws.rpc.ipc.Request;
import com.liws.rpc.utils.CommonUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liweisheng on 16/9/18.
 */
public class ServiceRegistry {
    private Map<String,Class> interfaces;

    private Map<String,Method> exposedMethods;

    private Object target;

    private String serviceName;

    private Class implClass;

    public ServiceRegistry(Class<?> clazz, String serviceName) throws Exception{
        this.serviceName = serviceName;
        this.implClass = clazz;
        this.exposedMethods = new HashMap<>();
        this.interfaces = new HashMap<>();
        this.target = clazz.newInstance();
        checkImplClazz(clazz);
        initIntrfs(clazz);
    }

    private void initIntrfs(Class<?> clazz) throws  Exception{
        Class[] intrfs = clazz.getInterfaces();

        for(Class intrf : intrfs){
            this.interfaces.put(intrf.getCanonicalName(),intrf);
            initMethods(intrf);
        }
    }

    private void initMethods(Class intrf){
        String intrfName = intrf.getCanonicalName();
        Method methods[] = intrf.getDeclaredMethods();

        StringBuilder uniqueMethodSignature = new StringBuilder();

        uniqueMethodSignature.append(intrfName);
        int truncIndex = uniqueMethodSignature.length();

        for(Method m : methods){
            uniqueMethodSignature.append("%").append(CommonUtils.getMethodSignatureName(m));
            this.exposedMethods.put(uniqueMethodSignature.toString(), m);
            uniqueMethodSignature.delete(truncIndex, uniqueMethodSignature.length());
        }
    }

    private void checkImplClazz(Class<?> clazz) throws Exception {
        int mod = clazz.getModifiers();
        if(Modifier.isAbstract(mod) || Modifier.isInterface(mod)){
            throw new Exception("should not be an abstract class or an interface");
        }
    }

    private Method getMethod(String methodSignatureName, String interfaceName) throws Exception{
        Class clazz = this.interfaces.get(interfaceName);

        if(null == clazz){
            return null;
        }

        Method m = this.exposedMethods.get(interfaceName + "%" + methodSignatureName);
        if(null != m){
            return m;
        }

        //TODO:抛出目标方法不存在异常.
        throw  new Exception("method do not exists");
    }


    public String invokeMethod(String methodSignatureName, String interfaceCanonicalName,String ...args) throws Exception{
        List<Object> argsAsObjects = null;

        Object ret = null;

        Method m = getMethod(methodSignatureName, interfaceCanonicalName);

        if(null != args){
            Class []argsTypeClasses = m.getParameterTypes();
            argsAsObjects = new ArrayList<>();

            for(int argIndex = 0;argIndex<args.length;++argIndex){
                Class argTypeClass = argsTypeClasses[argIndex];
                Object argAsObject = JSON.parseObject(args[argIndex],argTypeClass);
                argsAsObjects.add(argAsObject);
            }

            ret = m.invoke(this.target,argsAsObjects.toArray());
        }else{
            ret = m.invoke(this.target,null);
        }

        return JSON.toJSONString(ret);
    }


    public String invokeMethod(Request request) throws Exception{
        if(null == request){
            return null;
        }

        String methodSignatureName = request.getMethodSignatureName();
        String interfaceCanonicalName = request.getInterfaceCanonicalName();
        String[] argsJSON = (String[])request.getArgsJSONAsArray();

        return invokeMethod(methodSignatureName,interfaceCanonicalName,argsJSON);
    }

}


