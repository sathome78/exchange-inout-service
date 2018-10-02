package com.exrates.inout.domain.main;

import com.exrates.inout.domain.dto.NotificatorSubscription;
import com.exrates.inout.domain.enums.NotificatorSubscriptionStateEnum;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;

@Data
@Builder
public class TelegramSubscription implements NotificatorSubscription {


    private int id;
    private int userId;
    private String code;
    private NotificatorSubscriptionStateEnum subscriptionState;
    private String userAccount;
    private Long chatId;
    private String rawText;

    @Override
    public boolean isConnected() {
        return subscriptionState.isFinalState();
    }

    @Override
    public String getContactStr() {
        return userAccount;
    }

    @Override
    public BigDecimal getPrice() {
        return null;
    }

    @Tolerate
    public TelegramSubscription() {
    }
}
