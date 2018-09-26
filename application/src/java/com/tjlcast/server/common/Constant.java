package com.tjlcast.server.common;

/**
 * Created by tangjialiang on 2018/5/4.
 */
public interface Constant {

    // ===== zookeeper =====
    int ZK_SESSION_TIMEOUT      = 5000 ;
    int ZK_CONNECTION_TIMEOUT   = 10000;
    String ZK_REGISTRY_PATH     = "/registry" ;

    // ===== websocket =====
    String SOCKET_METRIC_ENDPOINT   = "/api/v1/smartruler/socket" ;
    String SOCKET_METRIC_APP        = "/plugins/metrics" ;
    String SOCKET_METRIC_RESPONSE   = "/plugins/metrics/response" ;
    /**
     * client send      /plugins/metrics/details/      inf: {host}/{port} // return
     * client subscribe /plugins/metrics/response/{host}/{port} //
     */
}