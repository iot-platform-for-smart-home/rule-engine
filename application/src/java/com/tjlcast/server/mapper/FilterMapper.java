package com.tjlcast.server.mapper;

import com.tjlcast.server.data.Filter;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by hasee on 2018/4/22.
 */
@Mapper
public interface FilterMapper {
    @Select("select filter.* from rule2Filter_Relation, filter where rule2Filter_Relation.ruleId=#{ruleId} and filter.filterId=rule2Filter_Relation.filterId")
    List<Filter> findFilterByRuleId(@Param("ruleId") Integer ruleId);

    @Select("select * from filter where filterId=#{filterId}")
    Filter getAFilter(@Param("filterId") Integer filterId) ;

    @Insert("INSERT INTO filter(type, name, jsCode) VALUES(#{type}, #{name}, #{jsCode})")
    @Options(useGeneratedKeys = true, keyProperty = "filterId", keyColumn = "filterId")
    int AddAFilter(Filter filter);

    @Update("UPDATE filter SET type = #{type}, name = #{name}, jsCode = #{jsCode} WHERE filterId = #{filterId}")
    Integer updateFilter(Filter filter);

    @Delete("DELETE FROM filter WHERE filterId=#{filterId}")
    void removeFilter(@Param("filterId")Integer filterId) ;

    @Delete("DELETE FROM filter")
    void removeAllFilter() ;


}
