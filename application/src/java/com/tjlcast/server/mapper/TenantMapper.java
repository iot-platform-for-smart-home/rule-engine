package com.tjlcast.server.mapper;

import com.tjlcast.server.data.Tenant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by tangjialiang on 2018/4/22.
 */
@Mapper
public interface TenantMapper {
    @Select("select * from t_tenant")
    List<Tenant> getAllTenants() ;
}
