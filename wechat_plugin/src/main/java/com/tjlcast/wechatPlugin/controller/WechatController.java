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


    /**
     * 接收平台消息
     * @param deviceMsg
     */
    @ConfirmActive
    @RequestMapping(value="send", method = RequestMethod.POST )
    public void wechatController(@RequestBody String deviceMsg){

        logger.info("deviceMsg: " + deviceMsg);
        logger.info("======= send templateNews =======");

        // 解析平台传过来的json数据,建立设备对象
         Device device = wechatService.processRequest(deviceMsg);

        pendingJobs.inc();

        // 发送模板消息
        messageUtil.pushTemplateNews(device.getOpenid(), device.getDevice(), device.getNumber(), device.getWarningMsg());
    }
}






//    @PostMapping(produces = "application/xml; charset=UTF-8")
//    public String post(@RequestBody String requestBody,
//                       @RequestParam("signature") String signature,
//                       @RequestParam("timestamp") String timestamp,
//                       @RequestParam("nonce") String nonce,
//                       @RequestParam(name = "encrypt_type", required = false) String encType,
//                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
//        this.logger.info("\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
//                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
//                        signature, encType, msgSignature, timestamp, nonce, requestBody);
//
//        if (!this.wechatService.checkSignature(timestamp, nonce, signature)) {
//            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
//        }
//
//        String out = null;
//        if (encType == null) {
//            // 明文传输的消息
//            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
//            WxMpXmlOutMessage outMessage = this.route(inMessage);
//            if (outMessage == null) {
//                return "";
//            }
//
//            out = outMessage.toXml();
//        } else if ("aes".equals(encType)) {
//            // aes加密的消息
//            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
//                    requestBody, this.wechatService.getWxMpConfigStorage(), timestamp,
//                    nonce, msgSignature);
//            this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
//            WxMpXmlOutMessage outMessage = this.route(inMessage);
//            if (outMessage == null) {
//                return "";
//            }
//
//            out = outMessage
//                    .toEncryptedXml(this.wechatService.getWxMpConfigStorage());
//        }
//
//        this.logger.debug("\n组装回复信息：{}", out);
//
//        return out;
//    }

//    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
//        try {
//            return this.router.route(message);
//        } catch (Exception e) {
//            this.logger.error(e.getMessage(), e);
//        }
//
//        return null;
//    }




