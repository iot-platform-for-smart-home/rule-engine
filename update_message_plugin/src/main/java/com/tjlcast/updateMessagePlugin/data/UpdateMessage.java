package com.tjlcast.updateMessagePlugin.data;

import com.google.gson.JsonObject;
import lombok.Data;

import java.math.BigInteger;

@Data
public class UpdateMessage {
    private Integer id;
    private String message;
    private String messageType;
    private BigInteger ts;
    private Integer tenantId;

    public UpdateMessage(Integer id, String message, String messageType, Long ts, Integer tenantId)
    {
        this.id = id;
        this.message = message;
        this.messageType = messageType;
        this.ts = BigInteger.valueOf(ts);
        this.tenantId = tenantId;
    }

    public UpdateMessage(JsonObject jsonObject){
        this.message = jsonObject.get("message").getAsString();
        this.messageType = jsonObject.get("messageType").getAsString();
        try {
            this.ts = BigInteger.valueOf(jsonObject.get("ts").getAsLong());
        }catch (Exception e){
            this.ts = BigInteger.valueOf(System.currentTimeMillis());
        }
        this.tenantId = jsonObject.get("tenantId").getAsInt();
    }
}
