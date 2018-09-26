package com.tjlcast.server.data;

import lombok.Data;

@Data
public class Plugin {
    String name;
    String url;         // host + port
    String describe;

    public Plugin(String name,String url,String describe) {
        this.name=name;
        this.url=url;
        this.describe=describe;
    }
}
