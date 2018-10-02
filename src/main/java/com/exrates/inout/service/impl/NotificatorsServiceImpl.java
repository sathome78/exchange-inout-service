package com.exrates.inout.service.impl;

import com.exrates.inout.dao.NotificatorPriceDao;
import com.exrates.inout.dao.NotificatorsDao;
import com.exrates.inout.domain.dto.Notificator;
import com.exrates.inout.domain.enums.NotificationTypeEnum;
import com.exrates.inout.service.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Log4j2(topic = "message_notify")
@Service
public class NotificatorsServiceImpl implements NotificatorsService {



    private final NotificatorsDao notificatorsDao;
    private final NotificatorPriceDao notificatorPriceDao;

    private final Map<String, NotificatorService> notificatorsMap;

    @Autowired
    public NotificatorsServiceImpl(NotificatorsDao notificatorsDao, NotificatorPriceDao notificatorPriceDao, Map<String, NotificatorService> notificatorsMap) {
        this.notificatorsDao = notificatorsDao;
        this.notificatorPriceDao = notificatorPriceDao;
        this.notificatorsMap = notificatorsMap;
    }

    public NotificatorService getNotificationService(Integer notificatorId) {
        Notificator notificator = Optional.ofNullable(this.getById(notificatorId))
                .orElseThrow(() -> new RuntimeException(String.valueOf(notificatorId)));
        return notificatorsMap.get(notificator.getBeanName());
    }

    public NotificatorService getNotificationServiceByBeanName(String beanName) {
        return notificatorsMap.get(beanName);
    }

    public Notificator getById(int id) {
        return notificatorsDao.getById(id);
    }

    public BigDecimal getMessagePrice(int notificatorId, int roleId) {
        return notificatorPriceDao.getFeeMessagePrice(notificatorId, roleId);
    }

}
