package com.exrates.inout.service.impl;

import com.exrates.inout.dao.NotificationDao;
import com.exrates.inout.domain.main.Email;
import com.exrates.inout.domain.main.NotificationEvent;
import com.exrates.inout.domain.main.NotificationOption;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.service.NotificationService;
import com.exrates.inout.service.SendMailService;
import com.exrates.inout.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Locale;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;

    private final UserService userService;

    private final SendMailService sendMailService;

    private final MessageSource messageSource;

    @Autowired
    public NotificationServiceImpl(NotificationDao notificationDao, UserService userService, SendMailService sendMailService, MessageSource messageSource) {
        this.notificationDao = notificationDao;
        this.userService = userService;
        this.sendMailService = sendMailService;
        this.messageSource = messageSource;
    }

    @Transactional(rollbackFor = Exception.class)
    public void notifyUser(String email, NotificationEvent cause, String titleCode, String messageCode,
                           Object[] messageArgs) {
        notifyUser(userService.getIdByEmail(email), cause, titleCode, messageCode, normalizeArgs(messageArgs));
    }

    @Transactional(rollbackFor = Exception.class)
    public void notifyUser(Integer userId, NotificationEvent cause, String titleCode, String messageCode,
                           Object[] messageArgs) {
        String lang = userService.getPreferedLang(userId);
        Locale locale = new Locale(StringUtils.isEmpty(lang) ? "EN" : lang);
        notifyUser(userId, cause, titleCode, messageCode, normalizeArgs(messageArgs), locale);
    }

    @Transactional(rollbackFor = Exception.class)
    public void notifyUser(Integer userId, NotificationEvent cause, String titleCode, String messageCode,
                           Object[] messageArgs, Locale locale) {
        String titleMessage = messageSource.getMessage(titleCode, null, locale);
        String message = messageSource.getMessage(messageCode, normalizeArgs(messageArgs), locale);
        notifyUser(userId, cause, titleMessage, message);
    }

    @Transactional(rollbackFor = Exception.class)
    public void notifyUser(Integer userId, NotificationEvent cause, String titleMessage, String message) {
        User user = userService.getUserById(userId);
        NotificationOption option = notificationDao.findUserOptionForEvent(userId, cause);
        if (option.isSendEmail()) {
            Email email = new Email();
            email.setSubject(titleMessage);
            email.setMessage(message);
            email.setTo(user.getEmail());
            sendMailService.sendInfoMail(email);
        }
    }


    private String[] normalizeArgs(Object... args) {
        return Arrays.toString(args).replaceAll("[\\[\\]]", "").split("\\s*,\\s*");
    }

}
