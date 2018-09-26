package com.tjlcast.basePlugin.controller;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.tjlcast.basePlugin.common.JsonUtils;
import com.tjlcast.basePlugin.service.DefaultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * Created by tangjialiang on 2018/5/29.
 */
@RestController("PluginController")
@RequestMapping("/api/plugin")
@Slf4j
public class PluginController {

    @Autowired
    private DefaultService defaultService;

    @Autowired
    WebApplicationContext applicationContext;

    private MetricRegistry metrics ;

    @Autowired
    public void setMetrics(MetricRegistry metrics) {
        this.metrics = metrics ;
    }

    @RequestMapping(value = "/active", method = RequestMethod.POST)
    @ResponseBody
    public String setActive(){
        defaultService.setState("ACTIVE");
        return "Plugin active";
    }

    @RequestMapping(value = "/suspend", method = RequestMethod.POST)
    @ResponseBody
    public String setSuspend(){
        defaultService.setState("SUSPEND");
        return "Plugin suspended";
    }

    @RequestMapping(value = "/state", method = RequestMethod.GET)
    @ResponseBody
    public String getState(){
        return defaultService.getState();
    }

    @RequestMapping(value = "/metrics", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getMetrics() {
        HashMap<String, Long> result = new HashMap<>() ;
        Map<String, Counter> counters = metrics.getCounters();
        counters.forEach(
                (k, v) -> {
                    result.put(k, v.getCount()) ;
                }
        );

        return JsonUtils.map2json(result).toString() ;
    }

    @RequestMapping(value = "/allUrls", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public List<String> getAllUrls(){
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, org.springframework.web.method.HandlerMethod> map = mapping.getHandlerMethods();
        List<String> urlList = new ArrayList<>();
        for (RequestMappingInfo info : map.keySet()){
            //获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            for (String url : patterns){
                if (url.startsWith("/api"))
                    urlList.add(url);
            }
        }
        return urlList;
    }
}
