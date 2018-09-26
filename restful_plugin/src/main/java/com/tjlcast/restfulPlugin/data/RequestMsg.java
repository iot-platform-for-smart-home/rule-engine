package com.tjlcast.restfulPlugin.data;

import com.google.gson.JsonObject;
import lombok.Data;

@Data
public class RequestMsg {
    private final JsonObject jsonObject;
    private final String url;
    private final String body;
    private final String method;

    public RequestMsg(String url, String body, String method) {
        this.jsonObject=null;
        this.url=url;
        this.body=body;
        this.method = method;
    }

    public RequestMsg(JsonObject jsonObject){
        String testBody;
        this.jsonObject = jsonObject;
        this.url = jsonObject.get("url").getAsString();
        try{
            testBody = jsonObject.get("body").getAsString();
        }catch(Exception exception){
            testBody = jsonObject.getAsJsonObject("body").toString();
        }
        this.body = testBody;
        this.method = jsonObject.get("method").getAsString();
    }
}
