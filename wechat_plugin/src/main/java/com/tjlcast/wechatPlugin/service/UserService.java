package com.tjlcast.wechatPlugin.service;

import java.util.List;

public interface UserService {
    // 获取tousers
    List<String> getAllTousers(List<String> mini_openids);
    String getTouser(String mini_openid);
    // 查数据库判断用户是否存在 及 是否关注微信公众号
    String judge_exist_and_follow(String unionid, String mini_openid, String oa_openid) throws Exception;
    List<String> getAllMiniOpenids(Integer customerid,String gatewayid);
    void get_and_insert_users(String access_token);

}
