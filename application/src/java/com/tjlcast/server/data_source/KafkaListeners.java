package com.tjlcast.server.data_source;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @Autowired
    DataSourceProcessor dataSourceProcessor ;

    private static final Logger log = LoggerFactory.getLogger(KafkaListeners.class) ;

    @KafkaListener(topics =  {"deviceData"})
    public void receive(String message){
        System.out.println(message);

        JsonObject jsonObj = (JsonObject)new JsonParser().parse(message);
        FromMsgMiddlerDeviceMsg fromMsgMiddlerDeviceMsg = new FromMsgMiddlerDeviceMsg(jsonObj);

        dataSourceProcessor.process(fromMsgMiddlerDeviceMsg);
    }
}
