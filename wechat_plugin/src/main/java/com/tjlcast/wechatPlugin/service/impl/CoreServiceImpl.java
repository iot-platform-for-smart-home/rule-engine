package com.tjlcast.wechatPlugin.service.impl;

import com.tjlcast.wechatPlugin.domain.Device;
import com.tjlcast.wechatPlugin.service.CoreService;
import com.tjlcast.wechatPlugin.util.weixinUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 核心服务类 （处理接收消息和推送消息）
 */
@Service("coreService")
public class CoreServiceImpl implements CoreService {

    private static Logger logger = LoggerFactory.getLogger(CoreServiceImpl.class);

    /**
     * 处理平台发来的请求
     * @param  deviceMsg
     */
    public Device processRequest(String deviceMsg) {
        logger.info("======= 解析 json 数据 =========");
        logger.info(" ");

        Device device = new Device();
        JsonObject jsonObject = (JsonObject)new JsonParser().parse(deviceMsg);

        // 获取字段信息
        device.setUserId(jsonObject.get("id").getAsString());  //用户id
        String nickname = jsonObject.get("nickname").getAsString(); //根据微信名获取openid
        String openid = weixinUtil.getOpenid(nickname);
//        System.out.println("succeed!  openid= " + openid);
        device.setOpenid(openid);
        device.setDevice(jsonObject.get("device").getAsString()); //设备名称
        device.setNumber(jsonObject.get("number").getAsString());  //设备编号
        device.setWarningMsg(jsonObject.get("warningMsg").getAsString());  //提示信息

        return device;
    }
}
