package com.tjlcast.wechatPlugin.mapping;


import com.bupt.wechatplugin.pojo.Auth;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description:利用Mybatis的注解实现数据的增删查改
 */
// 加上 Mapper 注解，在项目启动时可以扫描到
@Mapper
public interface UserMapper {
    @Results({
            @Result(property = "unionid", column = "unionid"),
            @Result(property = "mini_openid", column = "mini_openid"),
            @Result(property = "oa_openid", column = "oa_openid")
    })

    @Select("select mini_openid from auth where unionid = #{unionid}")
    String selectMiniOpenidByUnionid(@Param("unionid")String unionid);

    @Select("select oa_openid from auth where mini_openid = #{mini_openid}")
    String selectOaOpenidByMiniOpenid(@Param("mini_openid")String mini_openid);

    @Select("select * from auth where unionid = #{unionid}")
    Auth selectByUnionid(@Param("unionid")String unionid);

    @Insert("insert into auth(unionid,mini_openid,oa_openid) values(#{unionid},#{mini_openid},#{oa_openid})")
    void insert(@Param("unionid")String unionid, @Param("mini_openid")String mini_openid, @Param("oa_openid")String oa_openid);

    @Update("update auth set mini_openid = #{mini_openid} where unionid = #{unionid}")
    void updateMiniOpenid(@Param("unionid")String unionid, @Param("mini_openid")String mini_openid);

    @Update("update auth set oa_openid = #{oa_openid} where unionid = #{unionid}")
    void updateOaOpenid(@Param("unionid")String unionid, @Param("oa_openid")String oa_openid);

    @Select("select binder from user_relation where binded = #{customerid}")
    List<Integer> selectAllCustomers(@Param("customerid") Integer customerid);

    @Select("select openid from user_new where id = #{customerid}")
    String selectMiniOpenid(@Param("customerid")Integer customerid);

}
