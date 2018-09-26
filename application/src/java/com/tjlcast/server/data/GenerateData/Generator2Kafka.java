package com.tjlcast.server.data.GenerateData;

import com.google.gson.Gson;
import com.tjlcast.server.data_source.FromMsgMiddlerDeviceMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Created by tangjialiang on 2018/5/22.
 */
public class Generator2Kafka extends DefaultGenerator {

    // 系统日志
    private static final Logger LOGGER = LoggerFactory.getLogger(Generator2Stdout.class) ;

    private KafkaTemplate kafkaTemplate;

    public Generator2Kafka(int interTime, KafkaTemplate kafkaTemplate) {
        super(interTime);
        this.kafkaTemplate = kafkaTemplate ;
    }

    @Override
    protected void work(FromMsgMiddlerDeviceMsg msg) {
        System.out.println(Thread.currentThread().getName()) ;
        System.out.println(msg) ;

        Gson gs = new Gson();
        String ObjectStr = gs.toJson(msg);
        System.out.println(ObjectStr);

        String jsonStr = ObjectStr.replaceAll("\"jsonObj\":\\{\\},","");
        System.out.println(jsonStr);
        kafkaTemplate.send("deviceData","",jsonStr);
    }
}
