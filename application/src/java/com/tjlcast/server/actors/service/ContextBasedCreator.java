package com.tjlcast.server.actors.service;

import akka.japi.Creator;
import com.tjlcast.server.actors.ActorSystemContext;

/**
 * Created by tangjialiang on 2017/12/8.
 *
 *  在Actor环境中启动具体Actor的启动类，
 *  封装在具体Actor的内部类中作为启动类。
 *
 *  启动类主要用于在创建Actor的时候传递参数。
 *  usage：actorOf(Props.create(new DeviceActor.ActorCreator(...args)))
 */

public abstract class ContextBasedCreator<T> implements Creator<T> {
    protected final ActorSystemContext context ;

    public ContextBasedCreator(ActorSystemContext context) {
        super();
        this.context = context ;
    }
}
