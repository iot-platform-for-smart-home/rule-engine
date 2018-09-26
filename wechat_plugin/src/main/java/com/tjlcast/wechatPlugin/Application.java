package com.tjlcast.wechatPlugin;

//import org.mybatis.spring.annotation.MapperScan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Description : 启动类
 * @author
 * 2018年7月25日
 */
@SpringBootApplication  //等价于以默认属性使用@Configuration，@EnableAutoConfiguration和@ComponentScan
@PropertySource({"classpath:disconf.properties"})  //导入配置
@ImportResource({"classpath:disconf.xml"})   //引入disconf
@MapperScan("com.bupt.wechatplugin.mapping")  // 扫描目录下的Mapping方法
//@Component(basePackages = "")   //扫描目录下的组件
//@EnableAutoConfiguration   //自动配置
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
