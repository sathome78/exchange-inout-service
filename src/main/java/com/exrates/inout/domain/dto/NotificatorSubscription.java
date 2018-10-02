package com.exrates.inout.domain.dto;

import java.math.BigDecimal;

public interface NotificatorSubscription {

    String getContactStr();

    BigDecimal getPrice();

    boolean isConnected();
}
