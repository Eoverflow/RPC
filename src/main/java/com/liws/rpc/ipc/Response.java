package com.liws.rpc.ipc;

import com.alibaba.fastjson.JSON;
import com.liws.rpc.constant.RpcStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by liweisheng on 16/9/20.
 */
public class Response implements Serializable{
    private RpcStatus status;

    private String ret;

    private UUID requestUUID;

    public RpcStatus getStatus() {
        return status;
    }

    public void setStatus(RpcStatus status) {
        this.status = status;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }

    public void setRequestUUID(UUID requestUUID) {
        this.requestUUID = requestUUID;
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

    public static Response successReturn(Object ret, UUID uuid){
        String retAsString = JSON.toJSONString(ret);
        return createResponse(retAsString,uuid,RpcStatus.NORMAL);
    }

    public static Response successReturn(String ret,UUID uuid){
        return createResponse(ret,uuid,RpcStatus.NORMAL);
    }

    public static Response createResponse(String ret,UUID uuid,RpcStatus status){
        Response response = new Response();

        response.setStatus(status);
        response.setRet(ret);
        response.setRequestUUID(uuid);

        return response;
    }

    public static Response errorReturn(UUID uuid, RpcStatus status){
        return createResponse(status.getStatusMsg(),uuid,status);
    }

    public static Response exceptionReturn(Exception e, UUID uuid){
        return createResponse(e.toString(),uuid,RpcStatus.SERVER_EXCEPTION);
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", ret='" + ret + '\'' +
                ", requestUUID=" + requestUUID +
                '}';
    }
}
