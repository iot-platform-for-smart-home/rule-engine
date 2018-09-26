package SMSPlugin.service;

import com.tjlcast.basePlugin.service.DefaultService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SMSService extends DefaultService {

    @Override
    public Object service(Object[] data) {
        return null;
    }

    @Async
    public String sendSms(String phone,String text) throws Exception{
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://gbk.api.smschinese.cn");
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
        NameValuePair[] data ={ new NameValuePair("Uid", "youzi5"),new NameValuePair("Key", "d41d8cd98f00b204e980"),new NameValuePair("smsMob",phone),new NameValuePair("smsText",text)};
        post.setRequestBody(data);

        client.executeMethod(post);
//        Header[] headers = post.getResponseHeaders();
//        int statusCode = post.getStatusCode();
//        System.out.println("statusCode:"+statusCode);
//        for(Header h : headers)
//        {
//            System.out.println(h.toString());
//        }
        String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
        post.releaseConnection();
        return result; //打印返回消息状态
    }


}
