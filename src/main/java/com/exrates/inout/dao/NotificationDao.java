package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.NotificationDto;
import com.exrates.inout.domain.main.Notification;
import com.exrates.inout.domain.main.NotificationEvent;
import com.exrates.inout.domain.main.NotificationOption;

import java.util.List;

public interface NotificationDao {
    long createNotification(Notification notification);

    List<Notification> findAllByUser(Integer userId);

    List<NotificationDto> findByUser(Integer userId, Integer offset, Integer limit);

    boolean setRead(Long notificationId);

    boolean remove(Long notificationId);

    int setReadAllByUser(Integer userId);

    int removeAllByUser(Integer userId);

    List<NotificationOption> getNotificationOptionsByUser(Integer userId);

    void updateNotificationOptions(List<NotificationOption> options);

    NotificationOption findUserOptionForEvent(Integer userId, NotificationEvent event);

}

