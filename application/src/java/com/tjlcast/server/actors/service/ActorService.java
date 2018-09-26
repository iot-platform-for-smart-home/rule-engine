package com.tjlcast.server.actors.service;

import com.tjlcast.server.data_source.DataSourceProcessor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by tangjialiang on 2017/12/8.
 *
 * 每个service的Actor消息接口，用于定义该service的actor的消息
 */

public interface ActorService extends DataSourceProcessor
{

    public static enum Msg {
        get, post
    }

    public static class HttpResponse implements Serializable {
        @Setter
        @Getter
        public String response ;

        public HttpResponse(String response) {
            this.response = response ;
        }
    }
}
