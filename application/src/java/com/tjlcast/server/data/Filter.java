package com.tjlcast.server.data;

import com.google.gson.JsonObject;
import lombok.Data;

/**
 * Created by hasee on 2018/4/22.
 */
@Data
public class Filter {
    private Integer filterId;    // 过滤器 唯一Id (must)
    private String type;
    private String name;
    private String jsCode ;     // jsCode

    public Filter(Integer filterId, String type, String name, String jsCode) {
        this.filterId = filterId;
        this.type = type;
        this.name = name;
        this.jsCode = jsCode;
    }

    public Filter(JsonObject jsonObject)
    {
        try {
            this.filterId = jsonObject.get("filterId").getAsInt();
        } catch(NullPointerException e){

        }
        this.type=jsonObject.get("type").getAsString();
        this.name=jsonObject.get("name").getAsString();
        this.jsCode=jsonObject.get("jsCode").getAsString();
    }
}
