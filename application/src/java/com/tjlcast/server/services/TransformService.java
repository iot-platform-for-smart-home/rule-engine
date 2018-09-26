package com.tjlcast.server.services;

import com.tjlcast.server.data.Transform;
import com.tjlcast.server.mapper.TransformMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by tangjialiang on 2018/4/23.
 */

@Service
@Slf4j
public class TransformService{

    @Autowired
    private TransformMapper transformMapper;

    public List<Transform> getAllTransform() {
        List<Transform> all = transformMapper.findAll();
        return all ;
    }

    public Transform getByTransformId(Integer id) {
        Transform byId = transformMapper.findById(id);
        return byId ;
    }

    public List<Transform> getByRuleId(Integer id)
    {
        List<Transform> byRuleId = transformMapper.findByRuleId(id);
        return  byRuleId;
    }

    public boolean deleteAll() {
        transformMapper.deleteAll();
        return true ;
    }

    public boolean deleteById(Integer id) {
        transformMapper.deleteById(id);
        return true ;
    }

    public int addTransform(Transform tf) {
        int i =transformMapper.save(tf) ;
        return i ;
    }

    public Integer updataTransform (Transform transform){
        return transformMapper.updataTransform(transform);
    }

    //public boolean addTransform(List<Transform> tfs) {
    //    transformMapper.save((Iterable<? extends Transform>) tfs.iterator()) ;
    //    return true ;
    //}
}
