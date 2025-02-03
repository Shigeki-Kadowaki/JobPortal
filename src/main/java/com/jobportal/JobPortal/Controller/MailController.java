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


    public void sendMail(String from, String to, String text, String type) {;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject(type.equalsIgnoreCase("OA") ? "公欠届が却下されました" : "報告書が却下されました");
        message.setText("却下理由 : " + text + "\n\nこのメールは送信専用です。\n先生に伝えたい旨がある場合、再提出の備考欄に記入してください。");

        mailSender.send(message);
    }
}
