package com.tjlcast.mailPlugin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by tangjialiang on 2017/12/19.
 *
 */
@SpringBootApplication
@PropertySource({"classpath:disconf.properties"})
@ImportResource({"classpath:disconf.xml"})//引入disconf
public class Application {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }
}
