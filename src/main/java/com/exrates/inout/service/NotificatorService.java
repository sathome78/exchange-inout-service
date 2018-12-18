package com.exrates.inout.service;

import com.exrates.inout.domain.enums.NotificationTypeEnum;
import com.exrates.inout.exceptions.MessageUndeliweredException;

public interface NotificatorService {

    String sendMessageToUser(String userEmail, String message, String subject) throws MessageUndeliweredException;

    NotificationTypeEnum getNotificationType();
}
