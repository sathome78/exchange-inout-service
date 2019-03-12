package com.exrates.inout.controller;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.MerchantCurrency;
import com.exrates.inout.exceptions.InvalidAmountException;
import com.exrates.inout.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
public class CommonMerchantsController {

    private static final String MERCHANT = "MERCHANT";

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserOperationService userOperationService;

    @Autowired
    WithdrawService withdrawService;

    @Autowired
    RefillService refillService;

    @Autowired
    TransferService transferService;
    @Autowired
    private MessageSource messageSource;

    private static final Logger LOG = LogManager.getLogger("merchant");

    @RequestMapping(value = "/merchants/data", method = GET)
    public
    @ResponseBody
    List<MerchantCurrency> getMerchantsData() {
        List<Integer> currenciesId = currencyService
                .getAllActiveCurrencies()
                .stream()
                .mapToInt(Currency::getId)
                .boxed()
                .collect(Collectors.toList());
        return merchantService
                .getAllUnblockedForOperationTypeByCurrencies(currenciesId, OperationType.INPUT);
    }

    @RequestMapping(value = "/merchants/commission", method = GET)
    @ResponseBody
    public Map<String, String> getCommissions(final @RequestParam("type") OperationType type,
                                              final @RequestParam("amount") BigDecimal amount,
                                              final @RequestParam("currency") String currency,
                                              final @RequestParam("merchant") String merchant,
                                              Locale locale) {
        try {
            return merchantService.computeCommissionAndMapAllToString(amount, type, currency, merchant);
        } catch (InvalidAmountException e) {
            throw new InvalidAmountException(messageSource.getMessage(e.getMessage(), null, locale));
        }
    }

    @RequestMapping(value = "/merchants/warnings", method = GET)
    @ResponseBody
    public List<String> getMerchantWarnings(@RequestParam("type") OperationType type,
                                            @RequestParam("merchant") Integer merchantId,
                                            Locale locale) {
        return merchantService.getWarningsForMerchant(type, merchantId, locale);
    }


}
