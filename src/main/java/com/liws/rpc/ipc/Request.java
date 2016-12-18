package com.liws.rpc.ipc;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liweisheng on 16/9/19.
 */
public class Request implements Serializable{
    private UUID uuid;

    private String interfaceCanonicalName;

    private String serviceName;

    private String methodSignatureName;

    private List<Object> argsJSON;


    public Request(){
        uuid = UUID.randomUUID();
    }

    public Request(String interfaceCanonicalName,String serviceName, String methodSignatureName){
        uuid = UUID.randomUUID();
        this.interfaceCanonicalName = interfaceCanonicalName;
        this.serviceName = serviceName;
        this.methodSignatureName = methodSignatureName;
        this.argsJSON = new ArrayList<>();
    }

    public String getInterfaceCanonicalName() {
        return interfaceCanonicalName;
    }

    public void setInterfaceCanonicalName(String interfaceCanonicalName) {
        this.interfaceCanonicalName = interfaceCanonicalName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodSignatureName() {
        return methodSignatureName;
    }

    public void setMethodSignatureName(String methodSignatureName) {
        this.methodSignatureName = methodSignatureName;
    }

    public Object[] getArgsJSONAsArray(){
//        int argNum = argsJSON.size();
//        if(0 == argNum){
//            return null;
//        }
//
//        Object []args = new String[argNum];
//
//        int index = 0;
//
//        for(Object arg : argsJSON){
//            args[index] = arg;
//            ++index;
//        }
//
//        return args;
        return null;
    }

    public List<Object> getArgsJSON() {
        return argsJSON;
    }

    public void setArgsJSON(List<Object> argsJSON) {
        this.argsJSON = argsJSON;
    }

    public void addArg(Object arg){
        this.argsJSON.add(arg);
    }

    public Object getArg(int i){
        return argsJSON.get(i);
    }

    public byte[] getAsByteArray(){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            return baos.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "Request{" +
                "uuid=" + uuid +
                ", interfaceCanonicalName='" + interfaceCanonicalName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", methodSignatureName='" + methodSignatureName + '\'' +
                ", argsJSON=" + argsJSON +
                '}';
    }
}
