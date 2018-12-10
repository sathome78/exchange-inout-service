package com.exrates.inout.service.merchant.impl;

import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.MerchantInternalException;
import com.exrates.inout.exceptions.NotImplimentedMethod;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.exceptions.RefillRequestIdNeededException;
import com.exrates.inout.exceptions.RefillRequestNotFoundException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.merchant.OkPayService;
import com.exrates.inout.service.utils.WithdrawUtils;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

@Service
public class OkPayServiceImpl implements OkPayService {

    private static final Logger LOGGER = LogManager.getLogger("merchant");

    @Autowired
    private CryptoCurrencyProperties ccp;
    @Autowired
    private RefillRequestDao refillRequestDao;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private RefillService refillService;
    @Autowired
    private WithdrawUtils withdrawUtils;

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

        properties.put("ok_receiver", ccp.getPaymentSystemMerchants().getOkpay().getReceiver());
        properties.put("ok_currency", currency);
        properties.put("ok_invoice", String.valueOf(requestId));
        properties.put("ok_item_1_name", ccp.getPaymentSystemMerchants().getOkpay().getItemName());
        properties.put("ok_item_1_price", amountToPay.toString());
        properties.put("ok_s_title", ccp.getPaymentSystemMerchants().getOkpay().getSTitle());

        return generateFullUrlMap(ccp.getPaymentSystemMerchants().getOkpay().getUrl(), "POST", properties);
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

        LOGGER.info("Okpay processPayment: " + requestId + ", " + merchantTransactionId + ", " + currency + ", " + merchant + ", " + amount);
        RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));

        LOGGER.info("Okpay processPayment: " + refillRequest.toString());

        if (refillRequest.getAmount().equals(amount)
                && currency.equals(currencyService.getById(refillRequest.getCurrencyId()))
                && params.get("ok_txn_status").equals("completed")
                && params.get("ok_receiver_email").equals(ccp.getPaymentSystemMerchants().getOkpay().getReceiverEmail())) {
            LOGGER.info("Okpay processPayment: before requestAcceptDto");
            RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                    .requestId(requestId)
                    .merchantId(merchant.getId())
                    .currencyId(currency.getId())
                    .amount(amount)
                    .merchantTransactionId(merchantTransactionId)
                    .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
                    .build();
            LOGGER.info("Okpay processPayment: after requestAcceptDto");
            refillService.autoAcceptRefillRequest(requestAcceptDto);
            LOGGER.info("Okpay processPayment: after autoAcceptRefillRequest");
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
                .url(ccp.getPaymentSystemMerchants().getOkpay().getUrlReturn())
                .post(formBuilder.build())
                .build();
        final String returnResponse;
        try {
            returnResponse = client
                    .newCall(request)
                    .execute()
                    .body()
                    .string();
            LOGGER.info("returnResponse: " + returnResponse);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new MerchantInternalException(e);
        }
        return returnResponse.equals("VERIFIED");
    }

    @Override
    public boolean isValidDestinationAddress(String address) {
        return withdrawUtils.isValidDestinationAddress(address);
    }
}
