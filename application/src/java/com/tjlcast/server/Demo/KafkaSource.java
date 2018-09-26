package com.tjlcast.server.Demo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tjlcast.server.data_source.DataSourceProcessor;
import com.tjlcast.server.data_source.FromMsgMiddlerDeviceMsg;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * Created by tangjialiang on 2018/4/13.
 */

public class KafkaSource {

    @Autowired
    DataSourceProcessor dataSourceProcessor ;

    private static Consumer<String, String> consumer;

    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.108.218.64:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("Test_message"), new ConsumerRebalanceListener() {
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
            }

            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                //将偏移设置到最开始
                consumer.seekToBeginning(collection);
            }
        });
    }

    public  void grtMsgFromKafka(){

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                JsonObject jsonObj = (JsonObject)new JsonParser().parse(record.value());
                FromMsgMiddlerDeviceMsg fromMsgMiddlerDeviceMsg = new FromMsgMiddlerDeviceMsg(jsonObj);
                dataSourceProcessor.process(fromMsgMiddlerDeviceMsg);
            }
        }

    }
}
