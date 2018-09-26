package com.tjlcast.restfulPlugin.controller;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tjlcast.basePlugin.aop.ConfirmActive;
import com.tjlcast.basePlugin.common.ZKConstant;
import com.tjlcast.basePlugin.pluginManager.Plugin;
import com.tjlcast.restfulPlugin.data.RequestMsg;
import com.tjlcast.restfulPlugin.service.RestfulService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.concurrent.Future;

@RequestMapping("/api/v1/restfulplugin")
@Plugin(pluginInfo = "RestfulPlugin", registerAddr = ZKConstant.ZK_ADDRESS, detailInfo = "restfulplugin:8600|use for sending HTTP Request")
public class RestfulController {
    private final String controllerName = RestfulController.class.getSimpleName() ;

    private MetricRegistry metrics ;
    private Counter pendingJobs ;

    @Autowired
    public void setMetrics(MetricRegistry metrics) {
        this.metrics = metrics ;
        this.pendingJobs = this.metrics.counter(controllerName) ;
    }

    @Autowired
    private RestfulService restfulService;

    @ConfirmActive
    @ApiOperation(value = "send a request", notes = "send a request api")
    @ApiImplicitParam(name = "jsonStr", value = "{\n" +
            "\t\"url\": \"http://localhost:8400/api/test/sendDELETERequest\",\n" +
            "\t\"method\": \"POST\",\n" +
            "\t\"text\": {\"result\":\"success\"}\n" +
            "}", required = true)
    @RequestMapping(value = "/sendRequest", method = RequestMethod.POST)
    @ResponseBody
    public Future<String> sendRestfulRequest(@RequestBody String jsonStr) throws IOException {
        JsonObject jsonObj = (JsonObject)new JsonParser().parse(jsonStr);
        RequestMsg requestMsg = new RequestMsg(jsonObj);

        pendingJobs.inc();

        String s = restfulService.sendHTTPRequest(requestMsg);
        return new AsyncResult<String>(s);
    }
}
