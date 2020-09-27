package com.pan1.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;


@Service
@EnableAsync
public class SendEmailService {
/*
    @Autowired
    JavaMailSender javaMailSender;


 */
    //异步请求
    @Async
    public void sendMail(String emailAddress){
        //初始化邮件信息类
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("这是标题");
        simpleMailMessage.setFrom("");//输入你的QQ邮箱
        simpleMailMessage.setTo(emailAddress);
        //获取验证码
        //将验证码存放进邮箱
        simpleMailMessage.setText("hello World 这是你的验证码");
        //获取redis操作类
        //javaMailSender.send(simpleMailMessage);
        /*设置缓存*/

    }
}

