package com.exrates.inout.service;

import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
import com.exrates.inout.domain.main.NotificationsUserSetting;

public interface NotificationsSettingsService {

    NotificationsUserSetting getByUserAndEvent(int userId, NotificationMessageEventEnum event);
}
