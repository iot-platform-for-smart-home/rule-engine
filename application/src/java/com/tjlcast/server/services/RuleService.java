package com.tjlcast.server.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tjlcast.server.data.Rule;
import com.tjlcast.server.mapper.RuleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by tangjialiang on 2018/4/13.
 */

@Slf4j
@Service
public class RuleService {

    @Autowired
    RuleMapper ruleMapper;

    public List<Rule> findRuleByTenantId(Integer tenantId)
    {
        return ruleMapper.findRuleByTenantId(tenantId);
    }

    public List<Rule> findRuleByGatewayId(String gatewayId)
    {
        return ruleMapper.findRuleByGatewayId(gatewayId);
    }

    public Rule findRuleById(Integer ruleId)
    {
        return ruleMapper.findRuleById(ruleId);
    }

    public List<Rule> findGatewayAlarmRule(String gatewayId){
        return ruleMapper.getGatewayAlarmRule(gatewayId);
    }

    public List<Rule> findRuleByTenantIdAndText(Integer tenantId, String textSearch){return  ruleMapper.findRuleByTenantIdAndText(tenantId,textSearch);}

    public List<Rule> findRuleByGatewayIdAndText(String gatewayId, String textSearch){return  ruleMapper.findRuleByGatewayIdAndText(gatewayId,textSearch);}
    // add
    public int addRule(Rule rule) {
        int i =ruleMapper.addARule(rule) ;
        return i ;
    }

    public int addRule(JsonObject ruleJson) {
        Rule rule = new Gson().fromJson(ruleJson, Rule.class);
        return addRule(rule) ;
    }

    // remove
    public boolean removeAllRule() {
        ruleMapper.removeAllRule();
        return true ;
    }

    public boolean removeARule(Integer ruleId) {
        ruleMapper.removeRule(ruleId);
        return true ;
    }

    // get
    public List<Rule> getAllRule() {
        List<Rule> allRule = ruleMapper.getAllRule();
        return allRule ;
    }

    public boolean setRuleActive(Integer ruleId) {
        ruleMapper.setRuleActive(ruleId);
        return true;
    }

    public boolean setRuleSuspend(Integer ruleId) {
        ruleMapper.setRuleSuspend(ruleId);
        return true;
    }

    public Integer updateRule(Rule rule){
        return ruleMapper.updateRule(rule);
    }

}
