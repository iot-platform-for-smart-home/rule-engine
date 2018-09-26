package com.tjlcast.server.mapper;

import com.tjlcast.server.data.Rule2Filter;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Rule2FilterMapper {
    @Insert("INSERT INTO rule2Filter_Relation (ruleId, filterId) VALUES(#{ruleId}, #{filterId})")
    int addARelation(Rule2Filter rule2Filter);

    @Delete("DELETE FROM rule2Filter_Relation where ruleId = #{ruleId}")
    void removeRelation(Integer ruleId);

    @Delete("DELETE FROM rule2Filter_Relation where filterId = #{filterId}")
    void removeRelationByFilter(Integer filterId);

    @Delete("DELETE FROM rule2Filter_Relation")
    void removeAllRelation();
}
