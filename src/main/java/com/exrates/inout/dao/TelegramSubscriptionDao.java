package com.exrates.inout.dao;

import com.exrates.inout.domain.main.TelegramSubscription;

import java.util.Optional;

public interface TelegramSubscriptionDao {

    Optional<TelegramSubscription> getSubscribtionByCodeAndEmail(String code, String email);

    TelegramSubscription getSubscribtionByUserId(int userId);

    void updateSubscription(TelegramSubscription subscribtion);

    int create(TelegramSubscription subscription);
}
