package com.tjlcast.server.mapper;

import com.tjlcast.server.data.Transform;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by tangjialiang on 2018/4/23.
 */
@Mapper
public interface TransformMapper{

    @Select("select * from transform")
    List<Transform> findAll();

    @Select("select * from transform where transformId = #{transformId}")
    Transform findById(Integer transformId) ;

    @Select("select transform.* from rule2TransForm_Relation, transform where rule2TransForm_Relation.ruleId=#{ruleId} and transform.transformId=rule2TransForm_Relation.transformId")
    List<Transform> findByRuleId(Integer ruleId) ;

    @Delete("delete from transform")
    void deleteAll();

    @Delete("delete from transform where transformId = #{transformId}")
    void deleteById(Integer transformId);

    @Insert("insert into transform(name, url, method, requestBody) VALUES (#{name}, #{url}, #{method}, #{requestBody})")
    @Options(useGeneratedKeys = true, keyProperty = "transformId", keyColumn = "transformId")
    int save(Transform transform);

    @Update("UPDATE transform SET name = #{name}, url = #{url}, method = #{method}, requestBody = #{requestBody} WHERE transformId = #{transformId}")
    Integer updataTransform (Transform transform);


}
