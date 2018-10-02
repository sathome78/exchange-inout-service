package com.exrates.inout.dao;

import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
import com.exrates.inout.domain.enums.NotificationTypeEnum;

public interface NotificationMessagesDao {
    String gerResourceString(NotificationMessageEventEnum event, NotificationTypeEnum typeEnum);
}
