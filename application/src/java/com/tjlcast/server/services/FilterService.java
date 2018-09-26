package com.tjlcast.server.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tjlcast.server.data.Filter;
import com.tjlcast.server.mapper.FilterMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by hasee on 2018/4/22.
 */

@Slf4j
@Service
public class FilterService {

    @Autowired
    private FilterMapper filterMapper;

    public List<Filter> findFilterByRuleId(Integer ruleId)
    {
        return filterMapper.findFilterByRuleId(ruleId);
    }

    public int addFilter(JsonObject filterJson) {
        Filter filter = new Gson().fromJson(filterJson, Filter.class);
        return addFilter(filter) ;
    }

    public int addFilter(Filter filter) {
        int i = filterMapper.AddAFilter(filter);
        return i;
    }

    public boolean removeAll() {
        try {
            filterMapper.removeAllFilter();
            return true ;
        } catch (Exception e) {
            return false ;
        }
    }

    public boolean removeAFilter(Integer filterId) {
        try {
            filterMapper.removeFilter(filterId);
            return true ;
        } catch (Exception e) {
            return false ;
        }
    }

    public List<Filter> getAllFilter() {
        List<Filter> allFilter = getAllFilter();
        return allFilter ;
    }

    public Filter getAFilter(Integer filterId) {
        Filter aFilter = filterMapper.getAFilter(filterId);
        return aFilter ;
    }

    public Integer updateFilter(Filter filter){
        return filterMapper.updateFilter(filter);
    }
}
