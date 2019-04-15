package com.tjlcast.wechatPlugin.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Component
public class weixinUtil {

    private static String token = "weixinmp";
    private static final String APPID = "wxe4f16bf312643708";
    private static final String APPSECRET = "5e1518e38678bd75487e761e926dc7d1";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static String accesstoken;

    private static Logger logger = LoggerFactory.getLogger(new weixinUtil().getClass());

//    @Value("${wechatConfig.accesstoken}")
    public void setAccessToken(String accessToken) {
        accesstoken = accessToken;
    }

    public String getAccessToken() {
        if (accesstoken == null || accesstoken.equals("")) {
            updateAccesstoken();
        }
        return accesstoken;
    }

    /**
     * 定时任务，每90分钟更新accesstoken
     */
    @Lazy(false)
    @Scheduled(cron="0 */90 * * * ? ")   //每90分钟执行一次
    public void updateAccesstoken() {
        System.out.println("更新 accesstoken ...");
        // 小程序唯一标识 (在微信小程序管理后台获取)
        String wxAppid = APPID;
        // 小程序的 app secret (在微信小程序管理后台获取)
        String wxSecret = APPSECRET;
        String grant_type="client_credential";
        //封装请求数据
        String params = "grant_type=" + grant_type + "&secret=" + wxSecret + "&appid="+ wxAppid;
        //发送GET请求
        String url = "https://api.weixin.qq.com/cgi-bin/token?" + params;
        JSONObject jsonObject = CommonUtil.httpsRequest(url, "GET", null);
        if (null != jsonObject) {
            try {
                setAccessToken(jsonObject.getString("access_token"));
                System.out.println("更新完成，accesstoken = " + accesstoken);
            } catch (Exception e) {
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                logger.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
    }

    // ===============================================================
    // 分割线以下的函数暂时没有用

    /**
     * 验证签名
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] arr = new String[] { token, timestamp, nonce };
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        System.out.println(tmpStr);
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }

    /**
     *  get access_token
     */
//    public static AccessToken getAccessToken() {
//        AccessToken accessToken = new AccessToken();
//        String url = ACCESS_TOKEN_URL.replace("APPID",APPID).replace("APPSECRET", APPSECRET);
//        logger.info("get_access_token_url: " + url);
//
//        JSONObject jsonObject = doGetStr(url);
//        if(jsonObject!=null) {
//            try {
//                accessToken.setAccess_token(jsonObject.getString("access_token"));
//                accessToken.setExpires_in(jsonObject.getInteger("expires_in"));
//            } catch (Exception e) {
//                int errorCode = jsonObject.getInteger("errcode");
//                String errorMsg = jsonObject.getString("errmsg");
//                logger.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
//            }
//        }
//        return accessToken;
//    }

    /**
     * 向 url 发出请求
     * @param url
     * @return Json对象
     */
    public static JSONObject doGetStr(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if(entity!=null) {
                String result = EntityUtils.toString(entity,"UTF-8");
                jsonObject = JSONObject.parseObject(result);
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * POST请求
     * @param url
     * @param outStr
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static JSONObject doPostStr(String url, String outStr) throws ParseException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;
        httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity(),"UTF-8");
        jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }
}
