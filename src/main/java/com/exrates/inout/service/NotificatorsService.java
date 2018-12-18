package com.exrates.inout.service;


import com.exrates.inout.domain.dto.Notificator;

import java.math.BigDecimal;

public interface NotificatorsService {

    NotificatorService getNotificationService(Integer notificatorId);

    NotificatorService getNotificationServiceByBeanName(String beanName);

    Notificator getById(int id);

    BigDecimal getMessagePrice(int notificatorId, int roleId);
}
