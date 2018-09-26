package com.tjlcast.wechatPlugin.domain;

/**
 *  用户模型
 */
public class wechat {
    // 用户id
    private String id;
    // 用户姓名
    private String nickName;
    // 用户openid
    private String openid;

    public wechat(){

    }

    public wechat(String id, String nickName, String openid){
        this.id = id;
        this.nickName = nickName;
        this.openid = openid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", openid='" + openid + '\'' +
                '}';
    }
}
