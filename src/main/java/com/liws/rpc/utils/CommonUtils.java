package com.liws.rpc.utils;

import java.lang.reflect.Method;

/**
 * Created by liweisheng on 16/9/19.
 */
public class CommonUtils {

    public static String getMethodSignatureName(Method method){
        String methodName = method.getName();
        Class []argClasses = method.getParameterTypes();

        StringBuilder methodSignatureName = new StringBuilder();

        methodSignatureName.append(methodName);

        for(Class argClass : argClasses){
            String argTypeName = argClass.getCanonicalName();
            methodSignatureName.append("%").append(argTypeName);
        }

        return methodSignatureName.toString();
    }
}
