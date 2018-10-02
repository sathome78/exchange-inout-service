package com.exrates.inout.service;

import com.exrates.inout.domain.dto.NotificationResultDto;
import com.exrates.inout.domain.main.NotificationsUserSetting;
import org.springframework.transaction.annotation.Transactional;

public interface NotificationMessageService {

    @Transactional
    NotificationResultDto notifyUser(String userEmail, String message, String subject, NotificationsUserSetting setting);
}
