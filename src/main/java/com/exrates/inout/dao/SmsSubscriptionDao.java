package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.SmsSubscriptionDto;

import java.math.BigDecimal;

public interface SmsSubscriptionDao {

    int create(SmsSubscriptionDto dto);

    void update(SmsSubscriptionDto dto);

    SmsSubscriptionDto getByUserId(int userId);

    void updateDeliveryPrice(int userId, BigDecimal cost);
}
