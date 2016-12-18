package com.liws.rpc.constant;

/**
 * Created by liweisheng on 16/9/20.
 */
public enum RpcStatus {
    NORMAL(0, "success"),

    CONNECTION_LOSS(1, "loss ipc"),

    UNKOWN_METHOD(2, "unkown method"),

    UNKOWN_SERVICE(3, "unkown service"),

    SERVER_EXCEPTION(4,"exception occured in servcer");

    private int statusCode;
    private String statusMsg;

    private RpcStatus(int statusCode, String statusMsg){
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    @Override
    public String toString() {
        return "RpcStatus{" +
                "statusCode=" + statusCode +
                ", statusMsg='" + statusMsg + '\'' +
                '}';
    }
}
