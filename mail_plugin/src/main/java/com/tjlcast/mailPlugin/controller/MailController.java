package com.tjlcast.mailPlugin.controller;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tjlcast.basePlugin.aop.ConfirmActive;
import com.tjlcast.basePlugin.common.ZKConstant;
import com.tjlcast.basePlugin.pluginManager.Plugin;
import com.tjlcast.mailPlugin.data.MailData;
import com.tjlcast.mailPlugin.service.MailService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by tangjialiang on 2018/5/29.
 */
@RequestMapping("/api/v1/mailplugin")
@Plugin(pluginInfo = "MailPlugin", registerAddr = ZKConstant.ZK_ADDRESS, detailInfo = "mailplugin:8300|use for sending Email")
public class MailController {
    private final String controllerName = MailController.class.getSimpleName() ;

    private MetricRegistry metrics ;
    private Counter pendingJobs ;

    @Autowired
    public void setMetrics(MetricRegistry metrics) {
        this.metrics = metrics ;
        this.pendingJobs = this.metrics.counter(controllerName) ;
    }

    @Autowired
    private MailService mailService;

    @ConfirmActive
    @ApiOperation(value = "send a mail", notes = "send a mail api")
    @ApiImplicitParam(name = "jsonStr", value = "{\n" +
            "\t\"to\": [\"liyou@bupt.edu.cn\"],\n" +
            "\t\"subject\": \"传感器运行情况报告\",\n" +
            "\t\"text\": \"运行情况良好，祝你一路顺风\"\n" +
            "}", required = true)
    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    @ResponseBody
    public Future<String> sendMail(@RequestBody String jsonStr) throws Exception {
        JsonObject jsonObj = (JsonObject)new JsonParser().parse(jsonStr);
        MailData mailData = new MailData(jsonObj);

        List<String> toList=mailData.getTo();
        String[] to = new String[toList.size()];
        toList.toArray(to);

        pendingJobs.inc();

        mailService.sendEmail(to,mailData.getSubject(),mailData.getText());
        return new AsyncResult<String>("发送成功");
    }
}
