package com.tjlcast.basePlugin.service;

import com.codahale.metrics.Timer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by tangjialiang on 2018/5/29.
 */
public abstract class DefaultService<T, R> extends State{

    public static final String DEFALUT_ENCODING = "UTF-8";

    public abstract R service(T... data) ;

    @Resource
    private Timer responses;
}
