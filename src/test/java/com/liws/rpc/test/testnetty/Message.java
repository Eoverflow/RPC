package com.liws.rpc.test.testnetty;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by liweisheng on 16/9/13.
 */
public class Message implements Serializable{
    private UUID uuid;
    private String jsonContent;

    public Message(Object messageBody){
        this.uuid = UUID.randomUUID();
        this.jsonContent  = JSON.toJSONString(messageBody);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
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

    @Override
    public String toString() {
        return "Message{" +
                "uuid=" + uuid +
                ", jsonContent='" + jsonContent + '\'' +
                '}';
    }
}
