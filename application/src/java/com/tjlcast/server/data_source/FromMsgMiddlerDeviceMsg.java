package com.tjlcast.server.data_source;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tjlcast.server.aware.DeviceAwareMsg;
import com.tjlcast.server.aware.GatewayAwareMsg;
import com.tjlcast.server.aware.TenantAwareMsg;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tangjialiang on 2018/4/22.
 */
@Data
public class FromMsgMiddlerDeviceMsg implements TenantAwareMsg, GatewayAwareMsg, DeviceAwareMsg, Serializable {

    private final JsonObject jsonObj ;  // optional
    private final String deviceId ;     // required
    private final Integer tenantId ;    // required
    private final String gatewayId;   // required
    private final String deviceType;    // optional
    private final String name;
    private final String manufacture;
    private final String model;
    private final List<Item> data = new LinkedList<>();    // required



    public FromMsgMiddlerDeviceMsg(JsonObject jsonObj) {
        this.jsonObj = jsonObj ;
        this.deviceId = jsonObj.get("deviceId").getAsString() ;
        this.tenantId = Integer.valueOf(jsonObj.get("tenantId").getAsString()) ;
        this.gatewayId = jsonObj.get("gatewayId").getAsString();
        this.deviceType = jsonObj.get("deviceType").getAsString();
        this.name = jsonObj.get("name").getAsString();
        this.manufacture = jsonObj.get("manufacture").getAsString();
        this.model = jsonObj.get("model").getAsString();
        jsonObj.getAsJsonArray("data").forEach(x -> data.add(new Item((JsonObject) x)));
    }

    public FromMsgMiddlerDeviceMsg(String str) {
        this(new JsonParser().parse(str).getAsJsonObject()) ;
    }

    @Override
    public Integer getTenantId() {
        return this.tenantId ;
    }

    @Override
    public String getGatewayId() {
        return this.gatewayId ;
    }

    @Override
    public String getDeviceId() {
        return this.deviceId ;
    }

    public static class Builder {
        String deviceId ;
        int tenantId ;
        String gatewayId;

        JsonObject jsonObj = new JsonObject();  // todo
        String deviceType = "default";          // todo
        String name;
        String manufacture = "default";
        String model = "default";
        List<Item> items = new LinkedList<>();  // todo

        public Builder(int tenantId, String gatewayId, String deviceId) {
            this.deviceId = deviceId ;
            this.gatewayId = gatewayId;
            this.tenantId = tenantId ;
        }

        public Builder jsonObj(JsonObject jsonObject) {
            this.jsonObj = jsonObject ;
            return this ;
        }
        public Builder deviceType(String deviceType) {
            this.deviceType = deviceType ;
            return this ;
        }
        public Builder items(List<Item> items) {
            if (this.items.size()!=0) this.items.addAll(items) ;
            return this ;
        }
        public Builder aItem(Item item) {
            items.add(item) ;
            return this ;
        }
        public Builder name(String name){
            this.name = name ;
            return this ;
        }
        public Builder manufacture(String manufacture){
            this.manufacture=manufacture;
            return this;
        }
        public Builder model(String model){
            this.model = model;
            return this;
        }
        public FromMsgMiddlerDeviceMsg build() {
            return new FromMsgMiddlerDeviceMsg(this) ;
        }
    }

    private FromMsgMiddlerDeviceMsg(Builder builder) {
        this.jsonObj = builder.jsonObj ;
        this.deviceId = builder.deviceId ;
        this.tenantId = builder.tenantId ;
        this.gatewayId = builder.gatewayId;
        this.deviceType = builder.deviceType ;
        this.name = builder.name;
        this.manufacture = builder.manufacture;
        this.model = builder.model;
        if (builder.items.size()!=0) this.data.addAll(builder.items) ;
    }

}
