package com.tjlcast.server.services;

import com.tjlcast.server.data.Rule2Filter;
import com.tjlcast.server.data.Rule2Transform;
import com.tjlcast.server.mapper.Rule2FilterMapper;
import com.tjlcast.server.mapper.Rule2TransformMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Rule2TransformService {

    @Autowired
    private Rule2TransformMapper rule2TransformMapper;

    public int addARelation(Rule2Transform rule2Transform) {
        int i = rule2TransformMapper.addARelation(rule2Transform);
        return i;
    }

    public void removeRelation(Integer ruleId){
        rule2TransformMapper.removeRelation(ruleId);
    }

    public void removeAllRelation(){
        rule2TransformMapper.removeAllRelation();
    }

    public void removeRelationByTransform(Integer transformId){
        rule2TransformMapper.removeRelationByTransform(transformId);
    }
}
