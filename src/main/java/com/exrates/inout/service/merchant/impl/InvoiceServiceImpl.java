package com.exrates.inout.service.merchant.impl;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.exceptions.NotApplicableException;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.merchant.InvoiceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Service
@Log4j2
public class InvoiceServiceImpl implements InvoiceService {

  @Autowired
  private MessageSource messageSource;
  @Autowired
  private MerchantService merchantService;
  @Autowired
  private CurrencyService currencyService;

  @Override
  @Transactional
  public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
    throw new NotApplicableException("for " + withdrawMerchantOperationDto);
  }

  @Override
  @Transactional
  public Map<String, String> refill(RefillRequestCreateDto request) {
    String toWallet = String.format("%s: %s - %s",
        request.getRefillRequestParam().getRecipientBankName(),
        request.getAddress(),
        request.getRefillRequestParam().getRecipient());
    String message = messageSource.getMessage("merchants.refill.invoice",
        new String[]{request.getAmount().toPlainString().concat(currencyService.getCurrencyName(request.getCurrencyId()))
                , toWallet}, request.getLocale());
    return new HashMap<String, String>() {{
      put("message", message);
      put("walletNumber", request.getAddress());
    }};
  }

  @Override
  public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
    throw new NotApplicableException("for " + params);
  }
}

