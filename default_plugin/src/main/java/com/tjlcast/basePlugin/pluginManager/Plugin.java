package com.tjlcast.basePlugin.pluginManager;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by tangjialiang on 2018/5/29.
 */
@Target({ElementType.TYPE})         // 指明该注解适用的范围
@Retention(RetentionPolicy.RUNTIME) // 什么时候使用该注解
@Inherited                          // 该注解可以继承 --> cglib.
@Component                          // for spring scanning.
public @interface Plugin {

    String pluginInfo() ;                   // 在使用这个注解的时候，不用显示的给value赋值
    String registerAddr() ;                 // 向这个地址注册;
    String detailInfo() default "None" ;    // 插件的详细信息
}
