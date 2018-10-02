package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.NotificationPayTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Notificator {

    private int id;
    private String beanName;
    private BigDecimal messagePrice;
    private BigDecimal subscribePrice;
    private NotificationPayTypeEnum payTypeEnum;
    private boolean enabled;
    private String name;
    private boolean needSubscribe;
}
