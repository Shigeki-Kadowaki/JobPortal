package com.jobportal.JobPortal.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("mail")
public class MailController {
    @Autowired
    MailSender mailSender;


    public void sendMail(String to, String from, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject("公欠届が却下されました");
        message.setText(text);

        // メール送信を実施する。
        mailSender.send(message);
        System.out.println("send mail");
    }
}
