package com.exrates.inout.service;

import com.exrates.inout.domain.main.NotificationEvent;

import java.util.Locale;

public interface NotificationService {

    void notifyUser(Integer userId, NotificationEvent cause, String titleCode, String messageCode,
                    Object[] messageArgs);

    void notifyUser(String email, NotificationEvent cause, String titleCode, String messageCode,
                    Object[] messageArgs);

    void notifyUser(Integer userId, NotificationEvent cause, String titleCode, String messageCode,
                    Object[] messageArgs, Locale locale);

    void notifyUser(Integer userId, NotificationEvent cause, String titleMessage, String message);

}
