package com.tjlcast.server.data;

import com.google.gson.JsonObject;
import lombok.Data;

/**
 * Created by hasee on 2018/4/16.
 */
@Data
public class Rule {
    private Integer ruleId;                // rule 的唯一Id
    private Integer tenantId;          // 该tule的拥有者
    private String additional_info;
    private String name;       // 该rule的信息
    private String state;
    //private List<UUID> filters;     // 该rule配置的filter

    public Rule(Integer ruleId, Integer tenantId, String additional_info, String name, String state){
        this.ruleId=ruleId;
        this.tenantId=tenantId;
        this.additional_info=additional_info;
        this.name=name;
        this.state=state;
    }

    public Rule(JsonObject jsonObject){
        try {
            this.ruleId = jsonObject.get("ruleId").getAsInt();
        } catch(NullPointerException e){

        }
        this.tenantId=Integer.valueOf(jsonObject.get("tenantId").getAsString());
        this.additional_info=jsonObject.get("additional_info").getAsString();
        this.name=jsonObject.get("name").getAsString();
        this.state=jsonObject.get("state").getAsString();
    }
}
