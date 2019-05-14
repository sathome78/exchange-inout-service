package com.exrates.inout.service.impl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.dao.SmsSubscriptionDao;
import com.exrates.inout.domain.dto.SmsSubscriptionDto;
import com.exrates.inout.domain.enums.ActionType;
import com.exrates.inout.domain.enums.NotificationPayEventEnum;
import com.exrates.inout.domain.enums.NotificationTypeEnum;
import com.exrates.inout.domain.enums.NotificatorSubscriptionStateEnum;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.main.CompanyWallet;
import com.exrates.inout.domain.other.WalletOperationData;
import com.exrates.inout.exceptions.IncorrectSmsPinException;
import com.exrates.inout.exceptions.MessageUndeliweredException;
import com.exrates.inout.exceptions.PaymentException;
import com.exrates.inout.service.CompanyWalletService;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.NotificatorService;
import com.exrates.inout.service.NotificatorsService;
import com.exrates.inout.service.Subscribable;
import com.exrates.inout.service.UserService;
import com.exrates.inout.service.WalletService;
import com.exrates.inout.service.impl.epochta.EpochtaApi;
import com.exrates.inout.service.impl.epochta.Phones;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.exrates.inout.domain.other.WalletOperationData.BalanceType.ACTIVE;
import static com.exrates.inout.util.BigDecimalProcessing.doAction;

//@Log4j2(topic = "message_notify")
@Component
public class SmsNotificatorServiceImpl implements NotificatorService, Subscribable {

   private static final Logger log = LogManager.getLogger("message_notify");


    @Autowired
    private NotificatorsService notificatorsService;
    private final UserService userService;
    private final WalletService walletService;
    private final CurrencyService currencyService;
    private final SmsSubscriptionDao subscriptionDao;
    private final EpochtaApi smsService;
    private final CompanyWalletService companyWalletService;

    private static final String CURRENCY_NAME = "USD";
    private static final String SENDER = "Exrates";

    @Autowired
    public SmsNotificatorServiceImpl(UserService userService, WalletService walletService, CurrencyService currencyService, SmsSubscriptionDao subscriptionDao, EpochtaApi smsService, CompanyWalletService companyWalletService) {
        this.userService = userService;
        this.walletService = walletService;
        this.currencyService = currencyService;
        this.subscriptionDao = subscriptionDao;
        this.smsService = smsService;
        this.companyWalletService = companyWalletService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String sendMessageToUser(String userEmail, String message, String subject) throws MessageUndeliweredException {
        int userId = userService.getIdByEmail(userEmail);
        int roleId = userService.getUserRoleFromDB(userId).getRole();
        BigDecimal messagePrice = notificatorsService.getMessagePrice(getNotificationType().getCode(), roleId);
        SmsSubscriptionDto subscriptionDto = subscriptionDao.getByUserId(userService.getIdByEmail(userEmail));
        pay(
                messagePrice,
                subscriptionDto.getPrice(),
                userId,
                getNotificationType().name().concat(":").concat(NotificationPayEventEnum.BUY_ONE.name())
        );
        String xml = send(subscriptionDto.getContact(), message);
        try {
            BigDecimal cost = new BigDecimal(smsService.getValueFromXml(xml, "amount"));
            log.debug("last cost for number {} is {}", subscriptionDto.getContact(), cost);
            if (cost.compareTo(subscriptionDto.getPriceForContact()) != 0 && cost.compareTo(BigDecimal.ZERO) > 0) {
                subscriptionDao.updateDeliveryPrice(userId, cost);
            }
        } catch (Exception e) {
            log.error("can't get new price", e);
        }
        return String.valueOf(subscriptionDto.getContact());
    }

    @Transactional
    public String send(String contact, String message) {
        log.debug("send sms to {}, message {}", contact, message);
        String xml = smsService.sendSms(SENDER, message,
                new ArrayList<Phones>() {{
                    add(new Phones("id1", "", contact));
                }});
        log.debug("send sms status {}", xml);
        String status;
        try {
            status = smsService.getValueFromXml(xml, "status");
            if (Integer.parseInt(status) < 1) {
                throw new MessageUndeliweredException();
            }
        } catch (Exception e) {
            throw new MessageUndeliweredException();
        }
        return xml;
    }

    @Transactional
    public Object subscribe(Object subscriptionObject) {
        SmsSubscriptionDto recievedDto = (SmsSubscriptionDto) subscriptionObject;
        SmsSubscriptionDto userDto = Preconditions.checkNotNull(getByUserId(recievedDto.getUserId()));
        if (recievedDto.getCode().equals(userDto.getCode())) {
            userDto.setStateEnum(NotificatorSubscriptionStateEnum.getFinalState());
            userDto.setCode(null);
            userDto.setPriceForContact(userDto.getNewPrice());
            userDto.setContact(userDto.getNewContact());
            userDto.setNewPrice(null);
            userDto.setNewContact(null);
            createOrUpdate(userDto);
            return userDto;
        }
        throw new IncorrectSmsPinException();
    }

    public NotificationTypeEnum getNotificationType() {
        return NotificationTypeEnum.SMS;
    }

    @Transactional
    public BigDecimal pay(BigDecimal feePercent, BigDecimal deliveryAmount, int userId, String description) {
        BigDecimal feeAmount = doAction(deliveryAmount, feePercent, ActionType.MULTIPLY_PERCENT);
        BigDecimal totalAmount = doAction(feeAmount, deliveryAmount, ActionType.ADD);
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        WalletOperationData walletOperationData = new WalletOperationData();
        walletOperationData.setOperationType(OperationType.OUTPUT);
        walletOperationData.setWalletId(walletService.getWalletId(userId, currencyService.findByName(CURRENCY_NAME).getId()));
        walletOperationData.setBalanceType(ACTIVE);
        walletOperationData.setCommissionAmount(feeAmount);
        walletOperationData.setAmount(totalAmount);
        walletOperationData.setSourceType(TransactionSourceType.NOTIFICATIONS);
        walletOperationData.setDescription(description);
        WalletTransferStatus walletTransferStatus = walletService.walletBalanceChange(walletOperationData);
        if (!walletTransferStatus.equals(WalletTransferStatus.SUCCESS)) {
            throw new PaymentException();
        }
        CompanyWallet companyWallet = companyWalletService.findByCurrency(currencyService.findByName(CURRENCY_NAME));
        companyWalletService.deposit(companyWallet, new BigDecimal(0), feeAmount);
        return totalAmount;
    }


    private void createOrUpdate(SmsSubscriptionDto dto) {
        if (getByUserId(dto.getUserId()) == null) {
            subscriptionDao.create(dto);
        } else {
            subscriptionDao.update(dto);
        }
    }

    public SmsSubscriptionDto getByUserId(int userId) {
        return subscriptionDao.getByUserId(userId);
    }
}
