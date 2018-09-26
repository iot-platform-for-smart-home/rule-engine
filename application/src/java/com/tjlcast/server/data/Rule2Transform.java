package com.tjlcast.server.data;

import lombok.Data;

@Data
public class Rule2Transform {

    private Integer ruleId;
    private Integer transformId;

    public Rule2Transform(Integer ruleId,Integer transformId){
        this.ruleId=ruleId;
        this.transformId=transformId;
    }
}
