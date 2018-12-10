package com.exrates.inout.service.crypto_currencies;

import com.exrates.inout.dao.EDCAccountDao;
import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.MerchantInternalException;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.exceptions.RefillRequestFakePaymentReceivedException;
import com.exrates.inout.exceptions.RefillRequestMerchantException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.EDCServiceNode;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.TransactionService;
import com.exrates.inout.service.utils.WithdrawUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Log4j2(topic = "edc_log")
@Service
public class EDCServiceImpl implements EDCService {

    @Autowired
    private CryptoCurrencyProperties ccp;
    @Autowired
    TransactionService transactionService;
    @Autowired
    EDCAccountDao edcAccountDao;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RefillService refillService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private WithdrawUtils withdrawUtils;
    @Autowired
    EDCServiceNode edcServiceNode;

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
        log.info("Withdraw EDC: " + withdrawMerchantOperationDto.toString());
        edcServiceNode.transferFromMainAccount(
                withdrawMerchantOperationDto.getAccountTo(),
                withdrawMerchantOperationDto.getAmount());
        return new HashMap<>();
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        String address = getAddress();
        String message = messageSource.getMessage("merchants.refill.edr",
                new Object[]{address}, request.getLocale());
        return new HashMap<String, String>() {{
            put("address", address);
            put("message", message);
            put("qr", address);
        }};
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        checkTransactionByHistory(params);
        String merchantTransactionId = params.get("id");
        String address = params.get("address");
        String hash = params.get("hash");
        Currency currency = currencyService.findByName("EDR");
        Merchant merchant = merchantService.findByName("EDC");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(params.get("amount")));
        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .address(address)
                .merchantId(merchant.getId())
                .currencyId(currency.getId())
                .amount(amount)
                .merchantTransactionId(StringUtils.isEmpty(merchantTransactionId) ? hash : merchantTransactionId)
                .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
                .build();
        try {
            refillService.autoAcceptRefillRequest(requestAcceptDto);
        } catch (RefillRequestAppropriateNotFoundException e) {
            log.debug("RefillRequestAppropriateNotFoundException: " + params);
            Integer requestId = refillService.createRefillRequestByFact(requestAcceptDto);
            requestAcceptDto.setRequestId(requestId);
            refillService.autoAcceptRefillRequest(requestAcceptDto);
        }
    }

    private void checkTransactionByHistory(Map<String, String> params) {
        if (StringUtils.isEmpty(ccp.getOtherCoins().getEdc().getHistory())) {
            return;
        }
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(ccp.getOtherCoins().getEdc().getHistory() + ccp.getOtherCoins().getEdc().getToken() + "/" + params.get("address"))
                .build();
        final String returnResponse;

        try {
            returnResponse = client
                    .newCall(request)
                    .execute()
                    .body()
                    .string();
        } catch (IOException e) {
            throw new MerchantInternalException(e);
        }

        JsonParser parser = new JsonParser();
        try {
            JsonArray jsonArray = parser.parse(returnResponse).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                if (element.getAsJsonObject().get("id").getAsString().equals(params.get("id"))) {
                    if (element.getAsJsonObject().get("amount").getAsString().equals(params.get("amount"))) {
                        if (((JsonObject) element).getAsJsonObject("asset").get("symbol").getAsString().equals("EDC")) {
                            return;
                        }
                    }
                }
            }
        } catch (IllegalStateException e) {
            if ("Address not found".equals(parser.parse(returnResponse).getAsJsonObject().get("message").getAsString())) {
                throw new RefillRequestFakePaymentReceivedException(params.toString());
            } else {
                throw new RefillRequestMerchantException(params.toString());
            }
        }
        throw new RefillRequestFakePaymentReceivedException(params.toString());
    }

    private String getAddress() {
        final OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(60, TimeUnit.SECONDS);
        final FormEncodingBuilder formBuilder = new FormEncodingBuilder();
        formBuilder.add("account", ccp.getOtherCoins().getEdc().getMainAccount());
        formBuilder.add("hook", ccp.getOtherCoins().getEdc().getHook());
        final Request request = new Request.Builder()
                .url("https://receive.edinarcoin.com/new-account/" + ccp.getOtherCoins().getEdc().getToken())
                .post(formBuilder.build())
                .build();
        final String returnResponse;
        try {
            returnResponse = client
                    .newCall(request)
                    .execute()
                    .body()
                    .string();
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(returnResponse).getAsJsonObject();
            return object.get("address").getAsString();

        } catch (Exception e) {
            throw new MerchantInternalException("Unfortunately, the operation is not available at the moment, please try again later!");
        }
    }

    @Override
    public boolean isValidDestinationAddress(String address) {

        return withdrawUtils.isValidDestinationAddress(address);
    }
}
