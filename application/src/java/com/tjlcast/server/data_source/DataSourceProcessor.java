package com.tjlcast.server.data_source;

import com.tjlcast.server.data.Rule;

/**
 * Created by tangjialiang on 2018/4/22.
 */
public interface DataSourceProcessor {
    void process(FromMsgMiddlerDeviceMsg msg) ;     // send data to akka

    void process(Rule rule);                        // for rule ops
}
