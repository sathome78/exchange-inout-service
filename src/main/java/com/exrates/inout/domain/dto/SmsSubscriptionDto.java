package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.NotificatorSubscriptionStateEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;

@Data
@Builder
public class SmsSubscriptionDto implements NotificatorSubscription {

    private int id;
    private int userId;
    private String contact;
    private String code;
    private BigDecimal priceForContact;
    private NotificatorSubscriptionStateEnum stateEnum;
    private String newContact;
    private BigDecimal newPrice;

    @Override
    public String getContactStr() {
        return contact;
    }

    @Override
    public BigDecimal getPrice() {
        return priceForContact;
    }

    @Override
    public boolean isConnected() {
        return stateEnum.isFinalState();
    }

    @Tolerate
    public SmsSubscriptionDto() {
    }
}
