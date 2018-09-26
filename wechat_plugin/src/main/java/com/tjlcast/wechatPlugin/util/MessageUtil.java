package com.tjlcast.wechatPlugin.util;

import com.tjlcast.basePlugin.service.DefaultService;
import com.tjlcast.wechatPlugin.domain.*;
import com.thoughtworks.xstream.XStream;
import org.apache.http.ParseException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class MessageUtil extends DefaultService{
    @Override
    public Object service(Object[] data) {
        return null;
    }

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
     * @param touser openid
     * @param name   设备名称
     * @param number 设备编号
     * @param status 设备状态
     */
    public void pushTemplateNews(String touser,String name,String number,String status) {
        TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>();
        params.put("title",TemplateNews.item("您平台的设备出现异常状况！",TEXT_COLOR));
        params.put("name",TemplateNews.item(name,TEXT_COLOR));
        params.put("number",TemplateNews.item(number,TEXT_COLOR));
        params.put("status",TemplateNews.item(status,TEXT_COLOR));
        TemplateNews templateNews = new TemplateNews();
        templateNews.setTemplate_id(Templave_Id);
        templateNews.setTouser(touser);
        templateNews.setData(params);

        AccessToken access_token = weixinUtil.getAccessToken();
        System.out.println("access_token : " + access_token);

        String data = JsonUtil.toJsonString(templateNews);
        System.out.println("data: " + data);

        String url = TemplateNews_URL.replace("ACCESS_TOKEN",access_token.getAccess_token());
        System.out.println("send_message_url: " + url);
        try {
            weixinUtil.doPostStr(url,data);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

//    /*
//     * 主菜单
//     * */
//    public static String menuText() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("欢迎您的关注:\n\n");
//        sb.append("回复1、微信公众号介绍\n\n");
//        sb.append("回复2、开发者介绍\n\n");
//        sb.append("回复?、调出帮助菜单。");
//        return sb.toString();
//    }
//
//    public static String firstMenu() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("本公众号主要用于设备故障信息的推送反馈服务\n\n");
//        return sb.toString();
//    }
//
//    public static String secondMenu() {
//        StringBuffer sb = new StringBuffer();
//        sb.append("测试Demo\n\n");
//        return sb.toString();
//    }
