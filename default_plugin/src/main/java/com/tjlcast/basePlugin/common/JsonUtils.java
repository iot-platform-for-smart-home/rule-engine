package com.tjlcast.basePlugin.common;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by tangjialiang on 2018/5/24.
 */
public class JsonUtils {
    private JsonUtils() {}

    public static JsonObject map2json(Map<String, Long> map) {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, Long>entry : map.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue()); ;
        }
        return jsonObject ;
    }
}
