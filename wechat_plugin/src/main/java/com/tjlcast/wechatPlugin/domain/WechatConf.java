package com.tjlcast.wechatPlugin.domain;

public class WechatConf {
    //    private String appid = "wx3f4d110990c55e1d"; // 测试号
    private String appid = "wxe4f16bf312643708";
    //    private String appSecret = "645301efb63a9e82c3312040e0368a44";  // 测试号
    private String appSecret = "5e1518e38678bd75487e761e926dc7d1";
    private String token = "weixin";
    private String aesKey = "nUAj1trYmZWDHxf1AcW545ZLuaOSTTz5hivsTzacYUj";
    private String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private String template_send_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
    private String templateid = "_WdEdQiWPa8e0zZ02pIfX08eI98V2oPQVMQBCuCYt3U";
    private String message_text = "text";
    private String messaage_event = "event";
    private String text_color = "#DC143C";

    public String getText_color() {
        return text_color;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getAccess_token_url() {
        return access_token_url;
    }

    public void setAccess_token_url(String access_token_url) {
        this.access_token_url = access_token_url;
    }

    public String getTemplate_send_url() {
        return template_send_url;
    }

    public void setTemplate_send_url(String template_send_url) {
        this.template_send_url = template_send_url;
    }

    public String getTemplateid() { return templateid;}

}