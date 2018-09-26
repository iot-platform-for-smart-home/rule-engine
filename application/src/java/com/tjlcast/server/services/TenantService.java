package com.tjlcast.server.services;

import com.google.gson.JsonObject;
import com.tjlcast.server.data.Tenant;
import com.tjlcast.server.mapper.TenantMapper;
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
public class TenantService {

    @Autowired
    private TenantMapper tenantMapper ;

    // add
    public boolean addTenant(Tenant tenant) {
        // todo
        return false ;
    }

    public boolean addTenant(String tenantStr) {
        // todo
        return false ;
    }

    public boolean addTenant(JsonObject tenantJsonObj) {
        // todo
        return false ;
    }

    // get
    public List<Tenant> getAllTenant() {
        // todo
        return null ;
    }

    public Tenant getATenant(Integer id) {
        // todo
        return null ;
    }

    // remove
    public boolean removeAllTenant() {
        // todo
        return false ;
    }

    public boolean removeATenant(Integer id) {
        // todo
        return false ;
    }
}
