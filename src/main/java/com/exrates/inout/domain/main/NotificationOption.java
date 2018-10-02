package com.exrates.inout.domain.main;

import lombok.Data;

@Data
public class NotificationOption {
    private NotificationEvent event;
    private Integer userId;
    private boolean sendNotification;
    private boolean sendEmail;
}
