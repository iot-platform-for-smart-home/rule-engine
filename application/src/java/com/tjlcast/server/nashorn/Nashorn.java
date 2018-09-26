package com.tjlcast.server.nashorn;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by hasee on 2018/4/18.
 */
public class Nashorn {
    //private String js;
    //private String key;
    //private Double value;

    //public Nashorn(String js, String key, Double value)
    //{
    // this.js=js;
    // this.key=key;
    // this.value=value;
    // }
    private final String ENGINE_NAME = "nashorn" ;

    private final ScriptEngine engine ;

    public Nashorn(){
        this.engine = new ScriptEngineManager().getEngineByName(ENGINE_NAME) ;
    }

    public boolean invokeFunction(String js, String deviceId, String name, String manufacture, String deviceType, String model, Long ts, String key, Double value) throws ScriptException, NoSuchMethodException {
//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval(js);

        Invocable invocable = (Invocable) engine;
        Boolean result=(Boolean) invocable.invokeFunction("filter", name, deviceId, manufacture, deviceType, model, ts, key, value);
        return result;
    }

}
