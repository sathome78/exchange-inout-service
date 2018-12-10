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
import com.exrates.inout.exceptions.RefillRequestIdNeededException;
import com.exrates.inout.exceptions.RefillRequestNotFoundException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.service.AlgorithmService;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.merchant.NixMoneyService;
import com.exrates.inout.service.utils.WithdrawUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

@Service
public class NixMoneyServiceImpl implements NixMoneyService {

    private static final Logger LOGGER = LogManager.getLogger(NixMoneyServiceImpl.class);

    @Autowired
    private CryptoCurrencyProperties ccp;
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
        /**/
        Properties properties = new Properties() {{
            if (currency.equals("USD")) {
                put("PAYEE_ACCOUNT", ccp.getPaymentSystemMerchants().getNixmoney().getPayeeAccountUSD());
            }
            if (currency.equals("EUR")) {
                put("PAYEE_ACCOUNT", ccp.getPaymentSystemMerchants().getNixmoney().getPayeeAccountEUR());
            }
            put("PAYMENT_ID", requestId);
            put("PAYEE_NAME", ccp.getPaymentSystemMerchants().getNixmoney().getPayeeName());
            put("PAYMENT_AMOUNT", amountToPay);
            put("PAYMENT_URL", ccp.getPaymentSystemMerchants().getNixmoney().getPaymentUrl());
            put("NOPAYMENT_URL", ccp.getPaymentSystemMerchants().getNixmoney().getNoPaymentUrl());
            put("BAGGAGE_FIELDS", "PAYEE_ACCOUNT PAYMENT_AMOUNT PAYMENT_ID");
            put("STATUS_URL", ccp.getPaymentSystemMerchants().getNixmoney().getStatustUrl());
        }};
        /**/
        return generateFullUrlMap(ccp.getPaymentSystemMerchants().getNixmoney().getUrl(), "POST", properties);
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        Integer requestId = Integer.valueOf(params.get("PAYMENT_ID"));
        String merchantTransactionId = params.get("PAYMENT_BATCH_NUM");
        Currency currency = currencyService.findByName(params.get("PAYMENT_UNITS"));
        Merchant merchant = merchantService.findByName("Nix Money");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(params.get("PAYMENT_AMOUNT"))).setScale(9);

        RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));

        String passwordMD5 = algorithmService.computeMD5Hash(ccp.getPaymentSystemMerchants().getNixmoney().getPayeePassword()).toUpperCase();
        ;
        String V2_HASH = algorithmService.computeMD5Hash(params.get("PAYMENT_ID") + ":" + params.get("PAYEE_ACCOUNT")
                + ":" + params.get("PAYMENT_AMOUNT") + ":" + params.get("PAYMENT_UNITS") + ":" + params.get("PAYMENT_BATCH_NUM")
                + ":" + params.get("PAYER_ACCOUNT") + ":" + passwordMD5 + ":" + params.get("TIMESTAMPGMT")).toUpperCase();

        if (V2_HASH.equals(params.get("V2_HASH")) && refillRequest.getAmount().equals(amount)) {
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

    @Override
    public boolean isValidDestinationAddress(String address) {
        return withdrawUtils.isValidDestinationAddress(address);
    }
}
