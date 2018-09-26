package SMSPlugin.controller;

import SMSPlugin.data.SMSData;
import SMSPlugin.service.SMSService;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tjlcast.basePlugin.aop.ConfirmActive;
import com.tjlcast.basePlugin.common.ZKConstant;
import com.tjlcast.basePlugin.pluginManager.Plugin;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/v1/smsplugin")
@Plugin(pluginInfo = "SMSPlugin", registerAddr = ZKConstant.ZK_ADDRESS, detailInfo = "smsplugin:9000|use for sending SMS")
public class SMSController {

    private final String controllerName = SMSController.class.getSimpleName() ;

    private MetricRegistry metrics ;
    private Counter pendingJobs ;

    @Autowired
    public void setMetrics(MetricRegistry metrics) {
        this.metrics = metrics ;
        this.pendingJobs = this.metrics.counter(controllerName) ;
    }

    @Autowired
    private SMSService smsService;

    @ConfirmActive
    @ApiOperation(value = "send a sms", notes = "send a sms api")
    @ApiImplicitParam(name = "jsonStr", value = "{\n" +
            "\t\"phone\": \"[\"13120395827\"]\",\n" +
            "\t\"text\": \"您的接口和程序发送短信功能可用\"\n" +
            "}", required = true)
    @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
    @ResponseBody
    public Future<String> sendSms(@RequestBody String jsonStr) throws Exception{
        JsonObject jsonObj = (JsonObject)new JsonParser().parse(jsonStr);
        SMSData smsData = new SMSData(jsonObj);
        String result;

        pendingJobs.inc();
        result = smsService.sendSms(smsData.getPhones(),smsData.getText());


        return new AsyncResult<String>(result);
    }
}
