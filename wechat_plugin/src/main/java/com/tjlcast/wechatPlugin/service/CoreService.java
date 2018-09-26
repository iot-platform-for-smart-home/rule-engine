package com.tjlcast.wechatPlugin.service;

import com.tjlcast.wechatPlugin.domain.Device;

public interface CoreService {
    Device processRequest(String deviceMsg) ;
}
