package com.exrates.inout.dao;

import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
import com.exrates.inout.domain.main.NotificationsUserSetting;

public interface NotificationUserSettingsDao {

    NotificationsUserSetting getByUserAndEvent(int userId, NotificationMessageEventEnum event);

    int create(NotificationsUserSetting setting);

    void update(NotificationsUserSetting setting);
}
