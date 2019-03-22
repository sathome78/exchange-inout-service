package com.exrates.inout.service;

import com.exrates.inout.domain.dto.NotificationDto;
import com.exrates.inout.domain.main.Notification;
import com.exrates.inout.domain.main.NotificationEvent;
import com.exrates.inout.domain.main.NotificationOption;
import com.exrates.inout.util.CacheData;

import java.util.List;
import java.util.Locale;


public interface NotificationService {

    long createLocalizedNotification(Integer userId, NotificationEvent cause, String titleCode, String messageCode,
                                     Object[] messageArgs);

    long createLocalizedNotification(String userEmail, NotificationEvent cause, String titleCode, String messageCode,
                                     Object[] messageArgs);

    void notifyUser(String email, NotificationEvent cause, String titleCode, String messageCode,
                    Object[] messageArgs, Locale locale);

    void notifyUser(Integer userId, NotificationEvent cause, String titleCode, String messageCode,
                    Object[] messageArgs);

    void notifyUser(String email, NotificationEvent cause, String titleCode, String messageCode,
                    Object[] messageArgs);

    void notifyUser(Integer userId, NotificationEvent cause, String titleCode, String messageCode,
                    Object[] messageArgs, Locale locale);

    void notifyUser(Integer userId, NotificationEvent cause, String titleMessage, String message);

    List<Notification> findAllByUser(String email);

    List<NotificationDto> findByUser(String email, CacheData cacheData, Integer offset, Integer limit);

    boolean setRead(Long notificationId);

    boolean remove(Long notificationId);

    int setReadAllByUser(String email);

    int removeAllByUser(String email);

    List<NotificationOption> getNotificationOptionsByUser(Integer userId);

    void updateUserNotifications(List<NotificationOption> options);

    void updateNotificationOptionsForUser(int userId, List<NotificationOption> options);

}
