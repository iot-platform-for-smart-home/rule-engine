package com.tjlcast.server.message;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by tangjialiang on 2017/12/19.
 */

@Data
public class DeviceRecognitionMsg implements Serializable{

    private final UUID deviceId;
    private final String deviceName;
    private final String key;
    private final String ts;
    private final Double value;

    public DeviceRecognitionMsg(String deviceId, String deviceName, String key, String ts, String value) {
        this.deviceId = UUID.fromString(deviceId);
        this.deviceName = deviceName;
        this.key = key;
        this.ts = ts;
        this.value = Double.parseDouble(value);
    }

    public DeviceRecognitionMsg(UUID deviceId, String deviceName, String key, String ts, Double value) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.key = key;
        this.ts = ts;
        this.value = value;
    }
}
