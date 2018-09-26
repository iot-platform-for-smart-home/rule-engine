package com.tjlcast.server.data_source;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    private String key;
    private String value;
    private Long ts;

    public Item(JsonObject jobj) {
        this.key = jobj.get("key").getAsString();
        this.value = jobj.get("value").getAsString();
        this.ts =jobj.get("ts").getAsLong();
    }
}
