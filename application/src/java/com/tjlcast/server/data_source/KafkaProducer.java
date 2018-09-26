package com.tjlcast.server.data_source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


/**
 * Created by tangjialiang on 2018/5/22.
 */

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public String kafkaProduce(FromMsgMiddlerDeviceMsg msg){
        kafkaTemplate.send("deviceData","",msg);
        return "success";
    }
}
