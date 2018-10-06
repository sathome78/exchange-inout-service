package com.exrates.inout.service.merchant.impl;

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
import com.exrates.inout.service.AlgorithmService;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.merchant.PerfectMoneyService;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

@Service
public class PerfectMoneyServiceImpl implements PerfectMoneyService {

    private @Value("${perfectmoney.url}")
    String url;
    private @Value("${perfectmoney.accountId}")
    String accountId;
    private @Value("${perfectmoney.accountPass}")
    String accountPass;
    private @Value("${perfectmoney.payeeName}")
    String payeeName;
    private @Value("${perfectmoney.paymentSuccess}")
    String paymentSuccess;
    private @Value("${perfectmoney.paymentFailure}")
    String paymentFailure;
    private @Value("${perfectmoney.paymentStatus}")
    String paymentStatus;
    private @Value("${perfectmoney.USDAccount}")
    String usdCompanyAccount;
    private @Value("${perfectmoney.EURAccount}")
    String eurCompanyAccount;
    private @Value("${perfectmoney.alternatePassphrase}")
    String alternatePassphrase;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PerfectMoneyServiceImpl.class);

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private RefillRequestDao refillRequestDao;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private RefillService refillService;

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        throw new NotImplimentedMethod("for "+withdrawMerchantOperationDto);
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request){
        Integer orderId = request.getId();
        BigDecimal sum = request.getAmount();
        String currency = request.getCurrencyName();
        Number amountToPay = "GOLD".equals(currency) ? sum.toBigInteger() : sum.setScale(2, BigDecimal.ROUND_HALF_UP);
        /**/
        Properties properties = new Properties() {{
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
        }

    }

    private String computePaymentHash(Map<String, String> params) {
        final String passpphraseHash = algorithmService.computeMD5Hash(alternatePassphrase).toUpperCase();
        final String hashParams = params.get("PAYMENT_ID") +
                ":"+params.get("PAYEE_ACCOUNT") +
                ":"+params.get("PAYMENT_AMOUNT") +
                ":"+params.get("PAYMENT_UNITS") +
                ":"+params.get("PAYMENT_BATCH_NUM") +
                ":"+params.get("PAYER_ACCOUNT") +
                ":"+passpphraseHash +
                ":"+params.get("TIMESTAMPGMT");
        return algorithmService.computeMD5Hash(hashParams).toUpperCase();
    }

}