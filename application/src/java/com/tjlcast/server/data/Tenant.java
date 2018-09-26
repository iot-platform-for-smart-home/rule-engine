package com.tjlcast.server.data;

/**
 * Created by hasee on 2018/4/16.
 */
public class Tenant {
    private Integer tenantId;          // Tenant唯一Id
    private String tenantName;      // Tenant的姓名

    public Tenant(Integer tenantId, String tenantName) {
        this.tenantId = tenantId;
        this.tenantName = tenantName;
    }

    public Tenant(String tenantId, String tenantName) {
        this.tenantId = Integer.valueOf(tenantName);
        this.tenantName = tenantName;
    }
}
