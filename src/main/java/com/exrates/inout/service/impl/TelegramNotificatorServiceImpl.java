package com.exrates.inout.service.impl;

import com.exrates.inout.dao.TelegramSubscriptionDao;
import com.exrates.inout.domain.enums.NotificationTypeEnum;
import com.exrates.inout.domain.enums.NotificatorSubscriptionStateEnum;
import com.exrates.inout.domain.main.TelegramSubscription;
import com.exrates.inout.exceptions.MessageUndeliweredException;
import com.exrates.inout.exceptions.TelegramSubscriptionException;
import com.exrates.inout.service.NotificatorService;
import com.exrates.inout.service.Subscribable;
import com.exrates.inout.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2(topic = "message_notify")
@Component("telegramNotificatorServiceImpl")
public class TelegramNotificatorServiceImpl implements NotificatorService, Subscribable {

    private final TelegramSubscriptionDao subscribtionDao;
    private final UserService userService;
    private final TelegramBotService telegramBotService;

    @Autowired
    public TelegramNotificatorServiceImpl(TelegramSubscriptionDao subscribtionDao, UserService userService, TelegramBotService telegramBotService) {
        this.subscribtionDao = subscribtionDao;
        this.userService = userService;
        this.telegramBotService = telegramBotService;
    }


    @Transactional
    public Object subscribe(Object subscribeData) {
        TelegramSubscription subscriptionDto = (TelegramSubscription) subscribeData;
        String[] data = (subscriptionDto.getRawText()).split(":");
        String email = data[0];
        Optional<TelegramSubscription> subscriptionOptional = subscribtionDao.getSubscribtionByCodeAndEmail(subscriptionDto.getRawText(), email);
        TelegramSubscription subscription = subscriptionOptional.orElseThrow(TelegramSubscriptionException::new);
        NotificatorSubscriptionStateEnum nextState = subscription.getSubscriptionState().getNextState();
        if (subscription.getSubscriptionState().isFinalState()) {
            /*set New account for subscription if allready subscribed*/
            subscription.setChatId(subscriptionDto.getChatId());
            subscription.setUserAccount(subscriptionDto.getUserAccount());
            subscription.setCode(null);
        } else if (subscription.getSubscriptionState().isBeginState()) {
            subscription.setSubscriptionState(nextState);
            subscription.setChatId(subscriptionDto.getChatId());
            subscription.setUserAccount(subscriptionDto.getUserAccount());
            subscription.setCode(null);
        }
        subscribtionDao.updateSubscription(subscription);
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String sendMessageToUser(String userEmail, String message, String subject) {
        Optional<TelegramSubscription> subscriptionOptional = Optional.ofNullable(subscribtionDao.getSubscribtionByUserId(userService.getIdByEmail(userEmail)));
        TelegramSubscription subscription = subscriptionOptional.orElseThrow(MessageUndeliweredException::new);
        if (!subscription.getSubscriptionState().isFinalState()) {
            throw new MessageUndeliweredException();
        }
        telegramBotService.sendMessage(subscription.getChatId(), message);
        return subscription.getUserAccount();
    }

    public NotificationTypeEnum getNotificationType() {
        return NotificationTypeEnum.TELEGRAM;
    }

}
