package com.tjlcast.wechatPlugin.domain;

import java.util.List;
import java.util.TreeMap;

/**
 *  用户模型
 */
public class Device {
    // 用户id
    private String userId;
    // 用户名字
    private String nickName;
    // 用户对应的 openid
    private String openid;
    // 设备名称
    private String device;
    // 设备编号
    private String number;
    // 报警信息
    private String warningMsg;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getWarningMsg() {
        return warningMsg;
    }

    public void setWarningMsg(String warningMsg) {
        this.warningMsg = warningMsg;
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

    //    public static TreeMap<String, String> item(String name, String nember) {
//        TreeMap<String, String> params = new TreeMap<String, String>();
//
//        params.put("name", name);
//        params.put("nember", nember);
//        return params;
//    }

}
