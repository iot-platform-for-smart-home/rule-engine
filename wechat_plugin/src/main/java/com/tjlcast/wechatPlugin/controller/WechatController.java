package com.tjlcast.wechatPlugin.controller;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.tjlcast.basePlugin.aop.ConfirmActive;
import com.tjlcast.basePlugin.common.ZKConstant;
import com.tjlcast.basePlugin.pluginManager.Plugin;
import com.tjlcast.wechatPlugin.domain.Device;
import com.tjlcast.wechatPlugin.service.impl.CoreServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tjlcast.wechatPlugin.util.weixinUtil;
import com.tjlcast.wechatPlugin.util.MessageUtil;

@RestController
@RequestMapping("api/v1/wechatplugin/")
@Plugin(pluginInfo = "WechatPlugin", registerAddr = ZKConstant.ZK_ADDRESS, detailInfo = "wechatplugin:8900|use for sending Wechat message")
public class WechatController {

    private final String controllerName = WechatController.class.getSimpleName() ;

    private MetricRegistry metrics ;
    private Counter pendingJobs ;

    @Autowired
    public void setMetrics(MetricRegistry metrics) {
        this.metrics = metrics ;
        this.pendingJobs = this.metrics.counter(controllerName) ;
    }

    @Autowired
    private MessageUtil messageUtil;

    private CoreServiceImpl wechatService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public WechatController(CoreServiceImpl coreService) {
        this.wechatService = coreService;
    }

    /**
     * 微信开发者服务器验证
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return  若成功返回echostr
     */
    @ResponseBody
    @GetMapping("verify")
    public String authGet(
            @RequestParam(name = "signature", required = false) String signature,
            @RequestParam(name = "timestamp", required = false) String timestamp,
            @RequestParam(name = "nonce", required = false) String nonce,
            @RequestParam(name = "echostr", required = false) String echostr) {

        logger.info("======= verify start =======");
        logger.info("signature: "+ signature + "," +
                    "timestamp: " + timestamp + "," +
                    "nonce: " + nonce + "," +
                    "echostr: " + echostr);

        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        if (weixinUtil.checkSignature(signature, timestamp, nonce)) {
            logger.info("接入成功");
            return echostr;
        }
        logger.info("接入失败");
        return "error";
    }


//    GET、POST方式提时， 根据request header Content-Type的值来判断:
//
//    1. application/x-www-form-urlencoded， 可选（即非必须，因为这种情况的数据@RequestParam, @ModelAttribute也可以处理，当然@RequestBody也能处理）；
//    2. multipart/form-data, 不能处理（即使用@RequestBody不能处理这种格式的数据）；
//    3. 其他格式， 必须（其他格式包括application/json, application/xml等。这些格式的数据，必须使用@RequestBody来处理）；


    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    @ResponseBody
    public int getAllUsers(){
        // 获取access_token
        AccessToken access_token = weixinUtil.getAccessToken();
        if (null == access_token) return -1;
        // 获取关注用户列表并插入
        userService.get_and_insert_users(access_token.getAccess_token());
        return 0;
    }

    /**
     * 发送模板消息
     * @param alarmMSg 报警信息
     */
    @RequestMapping(value="/sendTemplateMsg", method = RequestMethod.POST )
    public void sendTemplateMsg(@RequestBody String alarmMSg){
        logger.info("============== send templateNews ==============");
        System.out.println("alarmMSg: " + alarmMSg);
        JSONObject msgJson = (JSONObject) JSONObject.parse(alarmMSg);
        String deviceName = msgJson.getString("deviceName");
        String deviceType = msgJson.getString("deviceType");
        String alarmDetail = msgJson.getString("alarmDetail");
        Integer customerId = msgJson.getInteger("customerId");
        String gatewayId = msgJson.getString("gatewayId");
        String template_id = conf.getTemplateid();
        String appid = conf.getAppid();

        // 根据customerid去account模块查找需要发送的用户的 mini_openid列表
        List<String> mini_openids = userService.getAllMiniOpenids(customerId, gatewayId);
        if(mini_openids == null)  return;

        // 查数据库获得Touser列表
        List<String> toUsers = userService.getAllTousers(mini_openids);
        if(toUsers == null)  return;

        // 发送模板消息
        for (String toUser: toUsers) {
            JSONObject data = new JSONObject();
            Date time = new Date();
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            data.put("first", JsonUtil.setItem("设备报警", conf.getText_color()));  // set first
            data.put("keyword1", JsonUtil.setItem(deviceName, null));  // 设备名
            data.put("keyword2", JsonUtil.setItem(ft.format(time), null));  // 报警时间
            data.put("keyword3", JsonUtil.setItem(alarmDetail, null));  // 报警内容
            data.put("remark", JsonUtil.setItem("请您及时处理！",null));
            TemplateNews tn = new TemplateNews(toUser, template_id, "","",data);
            MessageUtil.pushTemplateNews(tn);
        }
        logger.info("============== success ==============");
    }
}

