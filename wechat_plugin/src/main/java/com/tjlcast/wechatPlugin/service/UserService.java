package com.tjlcast.wechatPlugin.service;

import com.tjlcast.wechatPlugin.domain.wechat;

import java.util.List;

public interface UserService {
    /**
     * 查找所有用户
     * @return
     */
    List<wechat> findAll();

    /**
     * 根据微信名查找用户
     * @param nickname
     * @return
     */
    wechat findByNickName(String nickname);

    /**
     * 插入用户
     * @param id
     * @param nickname
     * @param openid
     */
    int insert(String id, String nickname, String openid);

    /**
     * 更新 openid
     * @param user
     */
    int updateOpenid(String nickname, String openid);

    /**
     * 根据 id 删除用户
     * @param id
     */
    int delete(String id);

}
