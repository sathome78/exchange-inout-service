package com.exrates.inout.dao;

import com.exrates.inout.domain.main.NotificationEvent;
import com.exrates.inout.domain.main.NotificationOption;

public interface NotificationDao {

    NotificationOption findUserOptionForEvent(Integer userId, NotificationEvent event);
}
