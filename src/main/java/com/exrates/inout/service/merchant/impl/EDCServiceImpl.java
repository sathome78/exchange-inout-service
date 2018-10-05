package com.exrates.inout.service.merchant.impl;

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
import com.exrates.inout.service.*;
import com.exrates.inout.service.merchant.EDCService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Log4j2(topic = "edc_log")
@Service
@PropertySource({"classpath:/merchants/edcmerchant.properties"})
public class EDCServiceImpl implements EDCService {

  private @Value("${edcmerchant.token}")
  String token;
  private @Value("${edcmerchant.main_account}")
  String main_account;
  private @Value("${edcmerchant.hook}")
  String hook;
  private @Value("${edcmerchant.history}")
  String history;

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
      /*
      Образец
      Predicate<RefillRequestFlatDto> predicate = (request) -> {
        return
            request.getMerchantId() == merchant.getId()
                && request.getCurrencyId() == currency.getId();
      };
      requestAcceptDto.setPredicate(predicate);*/
      refillService.autoAcceptRefillRequest(requestAcceptDto);
    } catch (RefillRequestAppropriateNotFoundException e) {
      log.debug("RefillRequestAppropriateNotFoundException: " + params);
      Integer requestId = refillService.createRefillRequestByFact(requestAcceptDto);
      requestAcceptDto.setRequestId(requestId);
      refillService.autoAcceptRefillRequest(requestAcceptDto);
    }
  }

  private void checkTransactionByHistory(Map<String, String> params) {
    if (StringUtils.isEmpty(history)) {
      return;
    }
    final OkHttpClient client = new OkHttpClient();
    final Request request = new Request.Builder()
        .url(history + token + "/" + params.get("address"))
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
            if (((JsonObject) element).getAsJsonObject("asset").get("symbol").getAsString().equals("EDC")){
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
    formBuilder.add("account", main_account);
    formBuilder.add("hook", hook);
    final Request request = new Request.Builder()
        .url("https://receive.edinarcoin.com/new-account/" + token)
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


}
