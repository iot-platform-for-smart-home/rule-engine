package com.tjlcast.server.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tjlcast.server.data.Device;
import com.tjlcast.server.mapper.DeviceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Created by hasee on 2018/4/17.
 */

@Slf4j
@Service
public class DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    public Device findDeviceById(Integer deviceId){
        return deviceMapper.findDeviceById(deviceId);
    }

    public boolean addDevice(JsonObject jsonObj) {
        Device device = new Gson().fromJson(jsonObj, Device.class) ;
        return addDevice(device) ;
    }

    public boolean addDevice(Device device) {
        try {
            int i = deviceMapper.addDevice(device);
            return i==1 ? true : false ;
        } catch (Exception e) {
            return false ;
        }
    }

    public Device getADevice(Integer id) {
        deviceMapper.findDeviceById(id) ;
        return null ;
    }

    public List<Device> getAllDevice(Integer id) {
        try {
            List<Device> allDeivce = deviceMapper.getAllDeivce();
            return allDeivce ;
        } catch (Exception e) {
            return null ;
        }
    }


    public boolean removeAllDevice() {
        try {
            deviceMapper.removeAllDevice();
            return true ;
        } catch (Exception e) {
            return false ;
        }
    }

}
