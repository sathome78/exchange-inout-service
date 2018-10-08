package com.exrates.inout.controller;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.MerchantCurrency;
import com.exrates.inout.domain.main.Payment;
import com.exrates.inout.domain.main.Wallet;
import com.exrates.inout.exceptions.InvalidAmountException;
import com.exrates.inout.service.*;
import com.exrates.inout.util.BigDecimalProcessing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.exrates.inout.domain.enums.OperationType.*;
import static com.exrates.inout.domain.enums.UserCommentTopicEnum.*;
import static java.util.stream.Collectors.toList;

@Controller
@Slf4j
public class CommonMerchantsController {

    private final CurrencyService currencyService;
    private final MerchantService merchantService;
    private final WalletService walletService;
    private final UserService userService;
    private final WithdrawService withdrawService;
    private final RefillService refillService;
    private final TransferService transferService;
    private final MessageSource messageSource;

    @Autowired
    public CommonMerchantsController(CurrencyService currencyService, MerchantService merchantService, WalletService walletService, UserService userService, WithdrawService withdrawService, RefillService refillService, TransferService transferService, MessageSource messageSource) {
        this.currencyService = currencyService;
        this.merchantService = merchantService;
        this.walletService = walletService;
        this.userService = userService;
        this.withdrawService = withdrawService;
        this.refillService = refillService;
        this.transferService = transferService;
        this.messageSource = messageSource;
    }

    @GetMapping(value = "/merchants/input")
    public ModelAndView inputCredits(@RequestParam("currency") String currencyName, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("globalPages/merchantsInput");
        try {

            Currency currency = currencyService.findByName(currencyName);
            Payment payment = new Payment(INPUT);
            BigDecimal minRefillSum = currencyService.retrieveMinLimitForRoleAndCurrency(userService.getUserRoleFromSecurityContext(), INPUT, currency.getId());
            Integer scaleForCurrency = currencyService.getCurrencyScaleByCurrencyId(currency.getId()).getScaleForRefill();
            List<Integer> currenciesId = Collections.singletonList(currency.getId());
            List<MerchantCurrency> merchantCurrencyData = merchantService.getAllUnblockedForOperationTypeByCurrencies(currenciesId, INPUT);
            refillService.retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies(merchantCurrencyData, principal.getName());
            List<String> warningCodeList = currencyService.getWarningForCurrency(currency.getId(), REFILL_CURRENCY_WARNING);
            boolean crypto = merchantCurrencyData.size() > 0 && !merchantCurrencyData.get(0).getProcessType().equals("CRYPTO");

            modelAndView.addObject("currency", currency);
            modelAndView.addObject("merchantCurrencyData", merchantCurrencyData);
            modelAndView.addObject("payment", payment);
            modelAndView.addObject("minRefillSum", minRefillSum);
            modelAndView.addObject("scaleForCurrency", scaleForCurrency);
            modelAndView.addObject("warningCodeList", warningCodeList);
            modelAndView.addObject("isAmountInputNeeded", crypto);

            return modelAndView;
        } catch (Exception e) {
            modelAndView = new ModelAndView("redirect:/dashboard");
            modelAndView.addObject("errorNoty", e.getClass().getSimpleName() + ": " + e.getMessage());
            return modelAndView;
        }
    }

    @GetMapping(value = "/merchants/output")
    public ModelAndView outputCredits(@RequestParam("currency") String currencyName, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("globalPages/merchantsOutput");
        try {

            OperationType operationType = OUTPUT;
            Currency currency = currencyService.findByName(currencyName);
            Wallet wallet = walletService.findByUserAndCurrency(userService.findByEmail(principal.getName()), currency);
            Payment payment = new Payment();
            payment.setOperationType(operationType);
            BigDecimal minWithdrawSum = currencyService.retrieveMinLimitForRoleAndCurrency(userService.getUserRoleFromSecurityContext(), operationType, currency.getId());
            Integer scaleForCurrency = currencyService.getCurrencyScaleByCurrencyId(currency.getId()).getScaleForWithdraw();
            List<Integer> currenciesId = Collections.singletonList(currency.getId());
            List<MerchantCurrency> merchantCurrencyData = merchantService.getAllUnblockedForOperationTypeByCurrencies(currenciesId, operationType);
            withdrawService.retrieveAddressAndAdditionalParamsForWithdrawForMerchantCurrencies(merchantCurrencyData);
            List<String> warningCodeList = currencyService.getWarningForCurrency(currency.getId(), WITHDRAW_CURRENCY_WARNING);

            modelAndView.addObject("currency", currency);
            modelAndView.addObject("wallet", wallet);
            modelAndView.addObject("balance", BigDecimalProcessing.formatNonePoint(wallet.getActiveBalance(), false));
            modelAndView.addObject("checkingBalance", wallet.getActiveBalance().compareTo(BigDecimal.ZERO) != 0);
            modelAndView.addObject("payment", payment);
            modelAndView.addObject("minWithdrawSum", minWithdrawSum);
            modelAndView.addObject("scaleForCurrency", scaleForCurrency);
            modelAndView.addObject("merchantCurrencyData", merchantCurrencyData);
            modelAndView.addObject("warningCodeList", warningCodeList);

            return modelAndView;
        } catch (Exception e) {
            modelAndView = new ModelAndView("redirect:/dashboard");
            modelAndView.addObject("errorNoty", e.getClass().getSimpleName() + ": " + e.getMessage());
            return modelAndView;
        }
    }

    @GetMapping("/merchants/transfer")
    public ModelAndView transfer(@RequestParam("currency") String currencyName, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("globalPages/transfer");
        try {
            OperationType operationType = USER_TRANSFER;
            Currency currency = currencyService.findByName(currencyName);
            Wallet wallet = walletService.findByUserAndCurrency(userService.findByEmail(principal.getName()), currency);
            Payment payment = new Payment();
            payment.setOperationType(operationType);
            BigDecimal minTransferSum = currencyService.retrieveMinLimitForRoleAndCurrency(userService.getUserRoleFromSecurityContext(), operationType, currency.getId());
            Integer scaleForCurrency = currencyService.getCurrencyScaleByCurrencyId(currency.getId()).getScaleForWithdraw();
            List<Integer> currenciesId = Collections.singletonList(currency.getId());
            List<MerchantCurrency> merchantCurrencyData = merchantService.getAllUnblockedForOperationTypeByCurrencies(currenciesId, operationType);
            transferService.retrieveAdditionalParamsForWithdrawForMerchantCurrencies(merchantCurrencyData);
            List<String> warningCodeList = currencyService.getWarningsByTopic(TRANSFER_CURRENCY_WARNING);

            modelAndView.addObject("currency", currency);
            modelAndView.addObject("wallet", wallet);
            modelAndView.addObject("balance", BigDecimalProcessing.formatNonePoint(wallet.getActiveBalance(), false));
            modelAndView.addObject("checkingBalance", wallet.getActiveBalance().compareTo(BigDecimal.ZERO) != 0);
            modelAndView.addObject("payment", payment);
            modelAndView.addObject("minTransferSum", minTransferSum);
            modelAndView.addObject("scaleForCurrency", scaleForCurrency);
            modelAndView.addObject("merchantCurrencyData", merchantCurrencyData);
            modelAndView.addObject("warningCodeList", warningCodeList);
            return modelAndView;
        } catch (Exception e) {
            modelAndView = new ModelAndView("redirect:/dashboard");
            modelAndView.addObject("errorNoty", e.getClass().getSimpleName() + ": " + e.getMessage());
            return modelAndView;
        }
    }

    @RequestMapping("/merchants/data")
    public List<MerchantCurrency> getMerchantsData() {
        List<Integer> currenciesId = currencyService
                .getAllCurrencies()
                .stream()
                .mapToInt(Currency::getId)
                .boxed()
                .collect(toList());
        return merchantService.getAllUnblockedForOperationTypeByCurrencies(currenciesId, INPUT);
    }

    @GetMapping("/merchants/commission")
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

    @GetMapping("/merchants/warnings")
    @ResponseBody
    public List<String> getMerchantWarnings(@RequestParam("type") OperationType type,
                                            @RequestParam("merchant") Integer merchantId,
                                            Locale locale) {
        return merchantService.getWarningsForMerchant(type, merchantId, locale);
    }


}
