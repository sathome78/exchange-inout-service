package com.exrates.inout.service.impl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.dao.NotificationMessagesDao;
import com.exrates.inout.domain.dto.NotificationResultDto;
import com.exrates.inout.domain.dto.Notificator;
import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
import com.exrates.inout.domain.enums.NotificationTypeEnum;
import com.exrates.inout.domain.main.NotificationsUserSetting;
import com.exrates.inout.exceptions.MessageUndeliweredException;
import com.exrates.inout.service.NotificationMessageService;
import com.exrates.inout.service.NotificatorService;
import com.exrates.inout.service.NotificatorsService;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//@Log4j2(topic = "message_notify")
@Component
public class NotificationMessageServiceImpl implements NotificationMessageService {

   private static final Logger log = LogManager.getLogger("message_notify");


    private final NotificatorsService notificatorsService;
    private final NotificationMessagesDao notificationMessagesDao;

    @Autowired
    public NotificationMessageServiceImpl(NotificatorsService notificatorsService, NotificationMessagesDao notificationMessagesDao) {
        this.notificatorsService = notificatorsService;
        this.notificationMessagesDao = notificationMessagesDao;
    }

    @Transactional
    public NotificationResultDto notifyUser(final String userEmail,
                                            final String message,
                                            final String subject,
                                            final NotificationsUserSetting setting) {
        Notificator notificator = Preconditions.checkNotNull(notificatorsService.getById(setting.getNotificatorId()));
        if (!notificator.isEnabled()) {
            notificator = notificatorsService.getById(NotificationTypeEnum.EMAIL.getCode());
        }
        NotificatorService service = notificatorsService.getNotificationServiceByBeanName(notificator.getBeanName());
        NotificationTypeEnum notificationTypeEnum = service.getNotificationType();
        String contactToNotify;
        try {
            contactToNotify = service.sendMessageToUser(userEmail, message, subject);
        } catch (Exception e) {
            log.error(e);
            if (notificationTypeEnum.getCode() != NotificationTypeEnum.EMAIL.getCode()) {
                NotificatorService emailService = notificatorsService.getNotificationService(NotificationTypeEnum.EMAIL.getCode());
                contactToNotify = emailService.sendMessageToUser(userEmail, message, subject);
                notificationTypeEnum = emailService.getNotificationType();
            } else {
                throw new MessageUndeliweredException();
            }
        }
        return getResponseString(setting.getNotificationMessageEventEnum(), notificationTypeEnum, contactToNotify);
    }

    private NotificationResultDto getResponseString(NotificationMessageEventEnum event, NotificationTypeEnum typeEnum, String contactToNotify) {
        String message = notificationMessagesDao.gerResourceString(event, typeEnum);
        return new NotificationResultDto(message, new String[]{contactToNotify});
    }
}
