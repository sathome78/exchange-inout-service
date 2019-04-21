package com.exrates.inout.service.impl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.domain.enums.NotificationTypeEnum;
import com.exrates.inout.domain.main.Email;
import com.exrates.inout.exceptions.MessageUndeliweredException;
import com.exrates.inout.service.NotificatorService;
import com.exrates.inout.service.SendMailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Log4j2(topic = "message_notify")
@Component
public class EmailNotificatorServiceImpl implements NotificatorService {

   private static final Logger log = LogManager.getLogger("message_notify");


    private final SendMailService sendMailService;

    @Autowired
    public EmailNotificatorServiceImpl(SendMailService sendMailService) {
        this.sendMailService = sendMailService;
    }

    public String sendMessageToUser(String userEmail, String message, String subject) throws MessageUndeliweredException {
        Email email = new Email();
        email.setTo(userEmail);
        email.setMessage(message);
        email.setSubject(subject);
        sendMailService.sendMailMandrill(email);
        return userEmail;
    }

    @Override
    public NotificationTypeEnum getNotificationType() {
        return NotificationTypeEnum.EMAIL;
    }
}
