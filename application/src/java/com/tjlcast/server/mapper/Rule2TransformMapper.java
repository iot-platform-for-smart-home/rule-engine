package com.tjlcast.server.mapper;

import com.tjlcast.server.data.Rule2Transform;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Rule2TransformMapper {
    @Insert("INSERT INTO rule2TransForm_Relation (ruleId, transformId) VALUES(#{ruleId}, #{transformId})")
    int addARelation(Rule2Transform rule2Transform);

    @Delete("DELETE FROM rule2TransForm_Relation where ruleId = #{ruleId}")
    void removeRelation(Integer ruleId);

    @Delete("DELETE FROM rule2TransForm_Relation")
    void removeAllRelation();

    @Delete("DELETE FROM rule2TransForm_Relation where transformId = #{transformId}")
    void removeRelationByTransform(Integer transformId);
}
