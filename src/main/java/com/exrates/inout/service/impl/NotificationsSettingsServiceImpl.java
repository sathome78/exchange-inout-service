package com.exrates.inout.service.impl;

import com.exrates.inout.dao.NotificationUserSettingsDao;
import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
import com.exrates.inout.domain.main.NotificationsUserSetting;
import com.exrates.inout.service.NotificationsSettingsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2(topic = "message_notify")
@Component
public class NotificationsSettingsServiceImpl implements NotificationsSettingsService {

    private final NotificationUserSettingsDao settingsDao;

    @Autowired
    public NotificationsSettingsServiceImpl(NotificationUserSettingsDao settingsDao) {
        this.settingsDao = settingsDao;
    }

    public NotificationsUserSetting getByUserAndEvent(int userId, NotificationMessageEventEnum event) {
        return settingsDao.getByUserAndEvent(userId, event);
    }
}
