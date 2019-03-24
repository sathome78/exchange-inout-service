package com.exrates.inout.service.impl;

import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.NotImplimentedMethod;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.exceptions.RefillRequestFakePaymentReceivedException;
import com.exrates.inout.exceptions.RefillRequestIdNeededException;
import com.exrates.inout.service.*;
import com.exrates.inout.util.WithdrawUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

@Service
@PropertySource("classpath:/merchants/payeer.properties")
public class PayeerServiceImpl implements PayeerService {

  private @Value("${payeer.url}") String url;
  private @Value("${payeer.m_shop}") String m_shop;
  private @Value("${payeer.m_desc}") String m_desc;
  private @Value("${payeer.m_key}") String m_key;


  private static final Logger logger = org.apache.log4j.LogManager.getLogger("merchant");

  @Autowired
  private AlgorithmService algorithmService;
  @Autowired
  private RefillService refillService;
  @Autowired
  private MerchantService merchantService;
  @Autowired
  private CurrencyService currencyService;
  @Autowired
  private WithdrawUtils withdrawUtils;
  @Autowired
  private GtagService gtagService;

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
    /**/
    Properties properties = new Properties() {{
      put("m_shop", m_shop);
      put("m_orderid", requestId);
      put("m_amount", amountToPay);
      put("m_curr", currency);
      String desc = algorithmService.base64Encode(m_desc);
      put("m_desc", desc);
      String sign = algorithmService.sha256(m_shop + ":" + requestId
          + ":" + amountToPay + ":" + currency
          + ":" + desc + ":" + m_key).toUpperCase();
      put("m_sign", sign);
    }};
    /**/
    return generateFullUrlMap(url, "POST", properties);
  }

  @Override
  public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
//    checkSign(params);
    Integer requestId = Integer.valueOf(params.get("m_orderid"));
    String merchantTransactionId = params.get("m_operation_id");
    Currency currency = currencyService.findByName(params.get("m_curr"));
    Merchant merchant = merchantService.findByName("Payeer");
    BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(params.get("m_amount")));

    RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
        .requestId(requestId)
        .merchantId(merchant.getId())
        .currencyId(currency.getId())
        .amount(amount)
        .merchantTransactionId(merchantTransactionId)
        .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
        .build();

    refillService.autoAcceptRefillRequest(requestAcceptDto);

    final String username = refillService.getUsernameByRequestId(requestId);

    logger.debug("Process of sending data to Google Analytics...");
    gtagService.sendGtagEvents(amount.toString(), currency.getName(), username);
  }

  private void checkSign(Map<String, String> params) {
    String sign = algorithmService.sha256(params.get("m_operation_id") + ":" + params.get("m_operation_ps")
        + ":" + params.get("m_operation_date") + ":" + params.get("m_operation_pay_date")
        + ":" + params.get("m_shop") + ":" + params.get("m_orderid")
        + ":" + params.get("m_amount") + ":" + params.get("m_curr") + ":" + params.get("m_desc")
        + ":" + params.get("m_status") + ":" + m_key).toUpperCase();
    if (params.get("m_sign")==null || !params.get("m_sign").equals(sign) || !"success".equals(params.get("m_status"))) {
      throw new RefillRequestFakePaymentReceivedException(params.toString());
    }
  }

  @Override
  public boolean isValidDestinationAddress(String address) {

    return withdrawUtils.isValidDestinationAddress(address);
  }


}
