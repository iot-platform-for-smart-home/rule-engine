package com.tjlcast.wechatPlugin.controller;

import com.alibaba.fastjson.JSONObject;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.tjlcast.basePlugin.aop.ConfirmActive;
import com.tjlcast.basePlugin.common.ZKConstant;
import com.tjlcast.basePlugin.pluginManager.Plugin;
import com.tjlcast.wechatPlugin.domain.TemplateNews;
import com.tjlcast.wechatPlugin.domain.WechatConf;
import com.tjlcast.wechatPlugin.service.UserService;
import com.tjlcast.wechatPlugin.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tjlcast.wechatPlugin.util.weixinUtil;
import com.tjlcast.wechatPlugin.util.MessageUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping("api/v1/wechatplugin/")
@Plugin(pluginInfo = "WechatPlugin", registerAddr = ZKConstant.ZK_ADDRESS, detailInfo = "wechatplugin:8900|use for sending Wechat message")
public class WechatController {

    private final String controllerName = WechatController.class.getSimpleName() ;

    private MetricRegistry metrics ;
    private Counter pendingJobs ;

    @Autowired
    private WechatConf conf;

    @Autowired
    private UserService userService;

    @Autowired
    private weixinUtil weixinUtil;

    @Autowired
    public void setMetrics(MetricRegistry metrics) {
        this.metrics = metrics ;
        this.pendingJobs = this.metrics.counter(controllerName) ;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());


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
    public void getAllUsers(){
        // 获取access_token
        String accesstoken = weixinUtil.getAccessToken();
        if (null == accesstoken  || "".equals(accesstoken)) {
            weixinUtil.updateAccesstoken();
            accesstoken = weixinUtil.getAccessToken();
        }

        // 获取关注用户列表并插入
        userService.get_and_insert_users(accesstoken);
    }

    /**
     * 发送模板消息
     * @param alarmMSg 报警信息
     */
    @ConfirmActive
    @RequestMapping(value="/sendTemplateMsg", method = RequestMethod.POST )
    public void sendTemplateMsg(@RequestBody String alarmMSg){

        System.out.println("alarmMSg: " + alarmMSg);
        JSONObject msgJson = (JSONObject) JSONObject.parse(alarmMSg);
        Integer customerId = msgJson.getInteger("customerId");
        String gatewayId = msgJson.getString("gatewayId");

        // 根据customerid去account模块查找需要发送的用户的 mini_openid列表
        List<String> mini_openids = userService.getAllMiniOpenids(customerId, gatewayId);
        if(mini_openids == null)  return;

        // 查数据库获得Touser列表
        List<String> toUsers = userService.getAllTousers(mini_openids);
        if(toUsers == null)  return;

        // 获取 access_token
        String access_token = weixinUtil.getAccessToken();

        // 发送模板消息
        logger.info("============== send templateNews ==============");
        String deviceName = msgJson.getString("deviceName");
        String deviceType = msgJson.getString("deviceType");
        String alarmDetail = msgJson.getString("alarmDetail");
        String template_id = conf.getTemplateid();
        for (int i = 0; i < toUsers.size(); i ++) {
            String toUser = toUsers.get(i);
            JSONObject data = new JSONObject();
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            ft.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            data.put("first", JsonUtil.setItem("设备报警", "#ED0000"));   // 消息首
            data.put("keyword1", JsonUtil.setItem(deviceName, "#000000"));       // 设备名
            data.put("keyword2", JsonUtil.setItem(ft.format(date), "#000000"));  // 报警时间
            data.put("keyword3", JsonUtil.setItem(alarmDetail, "#000000"));      // 报警内容
            data.put("remark", JsonUtil.setItem("请您及时处理！","#ED0000"));  // 消息尾
            TemplateNews tn = new TemplateNews(toUser, template_id, "","",data);

            // 计算出错的次数，大于三次则结束循环
            int error_time_count = 0;
            while(error_time_count < 3){
                int error = MessageUtil.pushTemplateNews(access_token, tn);

                // 解析返回结果
                if (error == 0) {  // 发送成功
                    break;
                } else if(error == 41001) { // errcode = 41001 , access_token失效, 更新重试
                    weixinUtil.updateAccesstoken();
                    access_token = weixinUtil.getAccessToken();
                    error_time_count ++;
                    continue;
                } else {  // 发送失败
                    error_time_count = 3;
                }
            }
            if(error_time_count>=3){
                logger.info("======== user[%s] error  =========", toUser);
            } else {
                logger.info("======== user[%s] success =========", toUser);
            }
        }
    }
}
