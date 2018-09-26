package com.tjlcast.server.data;
import lombok.Data;

/**
 * Created by hasee on 2018/4/16.
 */
@Data
public class Device {
    private Integer id ;               // system唯一id (must)
    private Integer tenantId;          // tenant唯一id (must)
    private String manufacture;     // 厂商
    private String deviceType;      // 设备
    private String model;           // 型号
    private String parentDeviceId;  // 父设备id

    public void setId(int id) {
        this.id = id ;
    }

    public Integer getId() {
        return id ;
    }

    public Integer getTenantId() {
        return tenantId ;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId ;
    }

    public Device(Integer id,Integer tenantId, String manufacture, String deviceType, String model, String parentDeviceId) {
        this.id = id ;
        this.tenantId = tenantId ;
        this.manufacture = manufacture ;
        this.deviceType = deviceType ;
        this.model = model ;
        this.parentDeviceId = parentDeviceId ;
    }

    public Device(String id, String tenantId, String manufacture, String deviceType, String model, String parentDeviceId) {
        this.id = Integer.valueOf(id) ;
        this.tenantId =Integer.valueOf(tenantId) ;
        this.manufacture = manufacture ;
        this.deviceType = deviceType ;
        this.model = model ;
        this.parentDeviceId = parentDeviceId ;
    }

    public Integer getTenantIdUUID() {
        return this.tenantId;
    }
}
