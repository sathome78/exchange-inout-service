package com.exrates.inout.service.impl;

import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.OkPayProperty;
import com.exrates.inout.service.*;
import com.exrates.inout.util.WithdrawUtils;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

@Service
public class OkPayServiceImpl implements OkPayService {

    private static final Logger logger = LogManager.getLogger("merchant");

    private String ok_receiver;
    private String ok_receiver_email;
    private String ok_item_1_name;
    private String ok_s_title;
    private String url;
    private String urlReturn;

    private RefillRequestDao refillRequestDao;
    private MerchantService merchantService;
    private CurrencyService currencyService;
    private RefillService refillService;
    private WithdrawUtils withdrawUtils;
    private GtagService gtagService;

    @Autowired
    public OkPayServiceImpl(RefillRequestDao refillRequestDao, MerchantService merchantService, CurrencyService currencyService, RefillService refillService,
                            WithdrawUtils withdrawUtils, GtagService gtagService, CryptoCurrencyProperties cryptoCurrencyProperties){
        this.refillRequestDao = refillRequestDao;
        this.merchantService = merchantService;
        this.currencyService = currencyService;
        this.refillService = refillService;
        this.withdrawUtils = withdrawUtils;
        this.gtagService = gtagService;

        OkPayProperty okPayProperty = cryptoCurrencyProperties.getPaymentSystemMerchants().getOkpay();
        this.ok_receiver = okPayProperty.getReceiver();
        this.ok_receiver_email = okPayProperty.getReceiverEmail();
        this.ok_item_1_name = okPayProperty.getItemName();
        this.ok_s_title = okPayProperty.getSTitle();
        this.url = okPayProperty.getUrl();
        this.urlReturn = okPayProperty.getUrlReturn();
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        throw new NotImplimentedMethod("for " + withdrawMerchantOperationDto);
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {

        Integer requestId = request.getId();
        if (requestId == null) {
            throw new RefillRequestIdNeededException(request.toString());
        }
        BigDecimal sum = request.getAmount();
        String currency = request.getCurrencyName();
        BigDecimal amountToPay = sum.setScale(2, BigDecimal.ROUND_HALF_UP);

        Properties properties = new Properties();

        properties.put("ok_receiver", ok_receiver);
        properties.put("ok_currency", currency);
        properties.put("ok_invoice", String.valueOf(requestId));
        properties.put("ok_item_1_name", ok_item_1_name);
        properties.put("ok_item_1_price", amountToPay.toString());
        properties.put("ok_s_title", ok_s_title);

        return generateFullUrlMap(url, "POST", properties);
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        if (!sendReturnRequest(params)) {
            throw new RefillRequestAppropriateNotFoundException(params.toString());
        }
        Integer requestId = Integer.valueOf(params.get("ok_invoice"));
        String merchantTransactionId = params.get("ok_txn_id");
        Currency currency = currencyService.findByName(params.get("ok_txn_currency"));
        Merchant merchant = merchantService.findByName("OkPay");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(params.get("ok_txn_gross"))).setScale(9);

        logger.info("Okpay processPayment: " + requestId + ", " + merchantTransactionId + ", " + currency + ", " + merchant + ", " + amount);
        RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));

        logger.info("Okpay processPayment: " + refillRequest.toString());

        if (refillRequest.getAmount().equals(amount)
                && currency.equals(currencyService.getById(refillRequest.getCurrencyId()))
                && params.get("ok_txn_status").equals("completed")
                && params.get("ok_receiver_email").equals(ok_receiver_email)) {
            logger.info("Okpay processPayment: before requestAcceptDto");
            RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                    .requestId(requestId)
                    .merchantId(merchant.getId())
                    .currencyId(currency.getId())
                    .amount(amount)
                    .merchantTransactionId(merchantTransactionId)
                    .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
                    .build();

            logger.info("Okpay processPayment: after requestAcceptDto");
            refillService.autoAcceptRefillRequest(requestAcceptDto);
            logger.info("Okpay processPayment: after autoAcceptRefillRequest");

            final String username = refillService.getUsernameByRequestId(requestId);

            logger.debug("Process of sending data to Google Analytics...");
            gtagService.sendGtagEvents(amount.toString(), currency.getName(), username);
        }

    }

    private boolean sendReturnRequest(Map<String, String> params) {

        final OkHttpClient client = new OkHttpClient();
        final FormEncodingBuilder formBuilder = new FormEncodingBuilder();
        formBuilder.add("ok_verify", "true");


        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }

        final Request request = new Request.Builder()
                .url(urlReturn)
                .post(formBuilder.build())
                .build();
        final String returnResponse;

        try {
            returnResponse = client
                    .newCall(request)
                    .execute()
                    .body()
                    .string();
            logger.info("returnResponse: " + returnResponse);
        } catch (IOException e) {
            logger.error(e);
            throw new MerchantInternalException(e);
        }

        return returnResponse.equals("VERIFIED");
    }

    @Override
    public boolean isValidDestinationAddress(String address) {

        return withdrawUtils.isValidDestinationAddress(address);
    }

}
