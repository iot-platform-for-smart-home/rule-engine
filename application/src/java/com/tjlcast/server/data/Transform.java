package com.tjlcast.server.data;

import com.google.gson.JsonObject;
import lombok.Data;

/**
 * Created by tangjialiang on 2018/4/23.
 */

@Data
public class Transform {
    private Integer transformId;
    private String name;
    private String url ;            // host + port + addr
    private String method ;
    private String requestBody;

    public Transform(Integer transformId,String name, String url, String method, String requestBody)
    {
        this.transformId=transformId;
        this.name=name;
        this.url=url;
        this.method=method;
        this.requestBody=requestBody;
    }

    public Transform(JsonObject jsonObject){
        try {
            this.transformId = jsonObject.get("transformId").getAsInt();
        } catch(NullPointerException e){

        }
        this.name=jsonObject.get("name").getAsString();
        this.url=jsonObject.get("url").getAsString();
        this.method=jsonObject.get("method").getAsString();
        this.requestBody=jsonObject.getAsJsonObject("requestBody").toString();
    }
}
