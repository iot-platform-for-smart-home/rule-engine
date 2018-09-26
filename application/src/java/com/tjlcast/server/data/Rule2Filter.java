package com.tjlcast.server.data;

import lombok.Data;

@Data
public class Rule2Filter {

    private Integer ruleId;
    private Integer filterId;

    public Rule2Filter(Integer ruleId,Integer filterId){
        this.ruleId=ruleId;
        this.filterId=filterId;
    }
}
