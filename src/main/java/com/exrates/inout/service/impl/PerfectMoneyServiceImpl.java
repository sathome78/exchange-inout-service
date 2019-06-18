package com.exrates.inout.service.impl;

import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.NotImplimentedMethod;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.exceptions.RefillRequestNotFoundException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.PerfectMoneyProperty;
import com.exrates.inout.service.*;
import com.exrates.inout.util.WithdrawUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

/**
 * @author Denis Savin (pilgrimm333@gmail.com)
 */
@Service
public class PerfectMoneyServiceImpl implements PerfectMoneyService {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PerfectMoneyServiceImpl.class);

    private String url;
    private String accountId;
    private String accountPass;
    private String payeeName;
    private String paymentSuccess;
    private String paymentFailure;
    private String paymentStatus;
    private String usdCompanyAccount;
    private String eurCompanyAccount;
    private String alternatePassphrase;

    private AlgorithmService algorithmService;
    private RefillRequestDao refillRequestDao;
    private MerchantService merchantService;
    private CurrencyService currencyService;
    private RefillService refillService;
    private WithdrawUtils withdrawUtils;
    private GtagService gtagService;

    @Autowired
    public PerfectMoneyServiceImpl(AlgorithmService algorithmService, RefillRequestDao refillRequestDao, MerchantService merchantService,
                                   CurrencyService currencyService, RefillService refillService, WithdrawUtils withdrawUtils, GtagService gtagService,
                                   CryptoCurrencyProperties cryptoCurrencyProperties){
        this.algorithmService = algorithmService;
        this.refillRequestDao = refillRequestDao;
        this.merchantService = merchantService;
        this.currencyService = currencyService;
        this.refillService = refillService;
        this.withdrawUtils = withdrawUtils;
        this.gtagService = gtagService;

        PerfectMoneyProperty perfectMoneyProperty = cryptoCurrencyProperties.getPaymentSystemMerchants().getPerfectmoney();
        this.url = perfectMoneyProperty.getUrl();
        this.accountId = perfectMoneyProperty.getAccountId();
        this.accountPass = perfectMoneyProperty.getAccountPass();
        this.payeeName = perfectMoneyProperty.getPayeeName();
        this.paymentSuccess = perfectMoneyProperty.getPaymentSuccess();
        this.paymentFailure = perfectMoneyProperty.getPaymentFailure();
        this.paymentStatus = perfectMoneyProperty.getPaymentStatus();
        this.usdCompanyAccount = perfectMoneyProperty.getUSDAccount();
        this.eurCompanyAccount = perfectMoneyProperty.getEURAccount();
        this.alternatePassphrase = perfectMoneyProperty.getAlternatePassphrase();
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        throw new NotImplimentedMethod("for " + withdrawMerchantOperationDto);
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        Integer orderId = request.getId();
        BigDecimal sum = request.getAmount();
        String currency = request.getCurrencyName();
        Number amountToPay = "GOLD".equals(currency) ? sum.toBigInteger() : sum.setScale(2, BigDecimal.ROUND_HALF_UP);
        /**/
        Properties properties = new Properties() {
            {
                put("PAYEE_ACCOUNT", currency.equals("USD") ? usdCompanyAccount : eurCompanyAccount);
                put("PAYEE_NAME", payeeName);
                put("PAYMENT_AMOUNT", amountToPay);
                put("PAYMENT_UNITS", currency);
                put("PAYMENT_ID", orderId);
                put("PAYMENT_URL", paymentSuccess);
                put("NOPAYMENT_URL", paymentFailure);
                put("STATUS_URL", paymentStatus);
                put("FORCED_PAYMENT_METHOD", "account");
            }
        };
        /**/
        return generateFullUrlMap(url, "POST", properties);
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        Integer requestId = Integer.valueOf(params.get("PAYMENT_ID"));
        String merchantTransactionId = params.get("PAYMENT_BATCH_NUM");
        Currency currency = params.get("PAYEE_ACCOUNT").equals(usdCompanyAccount) ? currencyService.findByName("USD") : currencyService.findByName("EUR");
        Merchant merchant = merchantService.findByName("Perfect Money");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(params.get("PAYMENT_AMOUNT"))).setScale(9);

        String hash = computePaymentHash(params);
        RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));

        if (params.get("V2_HASH").equals(hash) && refillRequest.getAmount().equals(amount)) {
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
    }

    private String computePaymentHash(Map<String, String> params) {
        final String passpphraseHash = algorithmService.computeMD5Hash(alternatePassphrase).toUpperCase();
        final String hashParams = params.get("PAYMENT_ID") +
                ":" + params.get("PAYEE_ACCOUNT") +
                ":" + params.get("PAYMENT_AMOUNT") +
                ":" + params.get("PAYMENT_UNITS") +
                ":" + params.get("PAYMENT_BATCH_NUM") +
                ":" + params.get("PAYER_ACCOUNT") +
                ":" + passpphraseHash +
                ":" + params.get("TIMESTAMPGMT");
        return algorithmService.computeMD5Hash(hashParams).toUpperCase();
    }

    @Override
    public boolean isValidDestinationAddress(String address) {

        return withdrawUtils.isValidDestinationAddress(address);
    }


}