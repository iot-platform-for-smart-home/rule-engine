package com.tjlcast.mailPlugin.service;

import com.codahale.metrics.Timer;
import com.tjlcast.basePlugin.service.DefaultService;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by tangjialiang on 2018/5/29.
 */
@Component
public class MailService extends DefaultService {
    @Override
    public Object service(Object[] data) {
        return null;
    }

    static final String DEFALUT_ENCODING = "UTF-8";

    @Resource
    private Timer responses;

    @Async
    public String sendEmail(String[] to,String subject,String text) throws Exception {
        final Timer.Context context = responses.time();

        JavaMailSenderImpl sender =initJavaMailSender();
        try {
            sendTextWithHtml(sender, to, subject, text);
            System.out.println("发送成功");
        }catch (Exception e){
            System.out.println(e);
        }
        finally{
            context.stop();
            return "";
        }
    }

    public static void sendTextWithHtml(JavaMailSenderImpl sender, String[] tos, String subject, String text)
            throws Exception {
        MimeMessage mailMessage = sender.createMimeMessage();
        initMimeMessageHelper(mailMessage, tos, sender.getUsername(), subject, text);
        // 发送邮件
        sender.send(mailMessage);

    }

    private static MimeMessageHelper initMimeMessageHelper(MimeMessage mailMessage, String[] tos, String from,
                                                           String subject, String text) throws MessagingException {
        return initMimeMessageHelper(mailMessage, tos, from, subject, text, true, false, DEFALUT_ENCODING);
    }


    private static MimeMessageHelper initMimeMessageHelper(MimeMessage mailMessage, String[] tos, String from,
                                                           String subject, String text, boolean isHTML, boolean multipart, String encoding) throws MessagingException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, multipart, encoding);
        messageHelper.setTo(tos);
        messageHelper.setFrom(from);
        messageHelper.setSubject(subject);
        // true 表示启动HTML格式的邮件
        messageHelper.setText(text, isHTML);

        return messageHelper;
    }


    public static JavaMailSenderImpl initJavaMailSender()
    {
        Properties properties=new Properties();
        properties.setProperty("mail debug","true");
        properties.setProperty("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.auth", "true");
        properties.put(" mail.smtp.timeout ", " 25000 ");

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setJavaMailProperties(properties);
        javaMailSender.setHost("220.181.12.16");
        javaMailSender.setUsername("liyou_test@163.com");   // 根据自己的情况,设置username
        javaMailSender.setPassword("liyounagi0929");        // 根据自己的情况, 设置password
        javaMailSender.setPort(465);
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }
}
