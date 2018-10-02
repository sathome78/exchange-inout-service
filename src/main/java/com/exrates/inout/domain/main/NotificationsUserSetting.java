package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class NotificationsUserSetting {

    private Integer id;
    private int userId;
    private NotificationMessageEventEnum notificationMessageEventEnum;
    private Integer notificatorId;

    @Tolerate
    public NotificationsUserSetting() {
    }
}
