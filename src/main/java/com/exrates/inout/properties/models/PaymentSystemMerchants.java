package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "payment-system-merchants")
public class PaymentSystemMerchants {

    private AdvCashProperty advcash;
    private InterkassaProperty interkassa;
    private LiqpayProperty liqpay;
    private NixMoneyProperty nixmoney;
    private OkPayProperty okpay;
    private PayeerProperty payeer;
    private PerfectMoneyProperty perfectmoney;
    private Privat24Property privat24;
    private YandexkassaProperty yandexkassa;
    private YandexMoneyProperty yandexmoney;
    private QiwiProperty qiwi;
}
