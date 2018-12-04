package com.tjlcast.wechatPlugin.util;

import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.tjlcast.wechatPlugin.domain.*;
import org.apache.http.ParseException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MessageUtil {
    /**
     *  返回消息类型 （text/image/news/link/location/event)
     */
    public static final String RESP_MESSAGE_TEXT = "text";
    public static final String RESP_MESSAGE_IMAGE = "image";
    public static final String RESP_MESSAGE_NEWS = "news";
    public static final String RESP_MESSAGE_LINK = "link";
    public static final String RESP_MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";
    /**
     *  事件类型 (subscribe/unsubscribe/click/view)
     */
    // 订阅 or 未关注用户扫描二维码
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    // 取消订阅
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
    // 自定义菜单点击事件
    public static final String MESSAGE_CLICK = "CLICK";
    // 点击自定义菜单跳转链接事件
    public static final String MESSAGE_VIEW = "VIEW";


    public static final String TEXT_COLOR = "#DC143C";
    public static final String TemplateNews_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
    public static final String Templave_Id = "2yF5SaZYPwPF1SdTAdezOFf3938LHa2Jtme5KyKsB1I";

    /**
     * 解析微信服务器发来的请求
     * @param request
     * @return 返回一个Map对象
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> map = new HashMap<String, String>();

        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }

    /**
     * 将文本消息对象转化为xml
     * @param textMessage 文本消息对象
     * @return xml 返回给微信服务器
     */
    public static String textMessageToXml(TextMessage textMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

    /**
     * 建立文本消息
     * @param toUserName (公众号名称)
     * @param fromUserName (用户openid）
     * @param content （消息内容）
     * @return XML
     */
    public static String initText(String toUserName,String fromUserName,String content) {
        TextMessage text = new TextMessage();
        text.setFromUserName(toUserName);
        text.setToUser(fromUserName);
        text.setMsgType(RESP_MESSAGE_TEXT);
        text.setCreateTime(new Date().getTime());
        text.setContent(content);
        return textMessageToXml(text);
    }

    /**
     * 图文消息转化为XML
     * @param newsMessage 图文消息对象
     * @return  返回XML
     */
    public static String newsMessageToXml(NewsMessage newsMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new News().getClass());
        return xstream.toXML(newsMessage);
    }

    /**
     * 建立图文消息
     * @param toUserName  公众号名称
     * @param fromUserName  用户openid
     * @return  XML
     */
    public static String initNewsMessage(String toUserName,String fromUserName) {
        String message = null;
        List<News> newsList = new ArrayList<News>();
        NewsMessage newsMessage = new NewsMessage();
        News news = new News();
        news.setTitle("815实验室介绍");
        news.setDescription("北邮科研楼815实验室");
        news.setPicUrl("https://45d74ab1.ngrok.io/Demo/images/img1.JPG");
        news.setUrl("https://45d74ab1.ngrok.io/Demo/HTML/index.html");

        newsList.add(news);

        newsMessage.setToUser(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(RESP_MESSAGE_NEWS);
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());
        message = newsMessageToXml(newsMessage);
        return message;
    }

    /**
     *  发送模板消息
     * @param templateNews
     */
    public static boolean pushTemplateNews(String access_token, TemplateNews templateNews) {
        System.out.println("access_token : " + access_token);
        String body = JsonUtil.toJsonString(templateNews);
        System.out.println("requestBody: " + body);
        String url = TemplateNews_URL.replace("ACCESS_TOKEN",access_token);
        System.out.println("send_message_url: " + url);
        try {
            JSONObject res = weixinUtil.doPostStr(url,body);
            int errcode = res.getInteger("errcode");
            String errmsg = res.getString("errmsg");
            long msgid = res.getLong("msgid");
            System.out.print(res.toJSONString());
            if(errcode == 0){
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
