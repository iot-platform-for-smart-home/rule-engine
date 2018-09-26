package com.tjlcast.wechatPlugin.mapping;

import com.tjlcast.wechatPlugin.domain.wechat;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description:利用Mybatis的注解实现数据的增删查改
 */
// 加上 Mapper 注解，在项目启动时可以扫描到
@Mapper
// 注册 mapper 接口
public interface UserMapper {

    @Select("select * from wechat")
    List<wechat> findAll();

    @Select("select * from wechat where nickname = #{nickname}")  // 用 #{ param } 的形式支持动态 sql
    wechat findByNickName(@Param("nickname") String nickname);  //  @Param中的参数名称要和 sql 语句中的参数名称一致

    @Insert("insert into wechat(id, nickname, openId) values(#{id},#{nickname}, #{openid})")
    int insert(@Param("id") String id, @Param("nickname") String nickname, @Param("openid") String openid);

    @Update("update wechat set openId=#{openid} where nickname=#{nickname}")
    int updateOpenid(@Param("nickname") String nickname, @Param("openid") String openid);

    @Delete("delete from wechat where id=#{id}")
    int delete(@Param("id") String id);



}
