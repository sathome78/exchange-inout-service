package com.exrates.inout.service.merchant.impl;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Transaction;
import com.exrates.inout.exceptions.NotImplimentedMethod;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.AlgorithmService;
import com.exrates.inout.service.TransactionService;
import com.exrates.inout.service.merchant.YandexKassaService;
import com.exrates.inout.service.utils.WithdrawUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

@Service
public class YandexKassaServiceImpl implements YandexKassaService {

    private static final Logger LOGGER = LogManager.getLogger("merchant");

    @Value("${yandex-kassa.shopId}")
    private String shopId;
    @Value("${yandex-kassa.scid}")
    private String scid;
    @Value("${yandex-kassa.shopSuccessURL}")
    private String shopSuccessURL;
    @Value("${yandex-kassa.paymentType}")
    private String paymentType;
    @Value("${yandex-kassa.key}")
    private String key;
    @Value("${yandex-kassa.password}")
    private String password;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AlgorithmService algorithmService;
    @Autowired
    private WithdrawUtils withdrawUtils;

    @Override
    @Transactional
    public Map<String, String> preparePayment(CreditsOperation creditsOperation, String email) {
        Transaction transaction = transactionService.createTransactionRequest(creditsOperation);
        BigDecimal sum = transaction.getAmount().add(transaction.getCommissionAmount());
        final Number amountToPay = sum.setScale(2, BigDecimal.ROUND_HALF_UP);

        final Map<String, String> properties = new TreeMap<>();

        properties.put("shopId", shopId);
        properties.put("scid", scid);
        properties.put("sum", String.valueOf(amountToPay));
        properties.put("customerNumber", email);
        properties.put("orderNumber", String.valueOf(transaction.getId()));
        properties.put("shopSuccessURL", shopSuccessURL);
        properties.put("paymentType", paymentType);
        properties.put("key", key);

        return properties;
    }

    @Override
    @Transactional
    public boolean confirmPayment(final Map<String, String> params) {
        Transaction transaction;
        try {
            transaction = transactionService.findById(Integer.parseInt(params.get("orderNumber")));
            if (transaction.isProvided()) {
                return true;
            }
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error(e);
            return false;
        }
        String checkSignature = algorithmService.computeMD5Hash(params.get("action") + ";" + params.get("orderSumAmount") + ";" + params.get("orderSumCurrencyPaycash") + ";"
                + params.get("orderSumBankPaycash") + ";" + params.get("shopId") + ";" + params.get("invoiceId") + ";"
                + params.get("customerNumber") + ";" + password).toUpperCase();

        if (checkSignature.equals(params.get("md5"))) {
            transactionService.provideTransaction(transaction);
            LOGGER.debug("Payment successful.");
            return true;
        }
        LOGGER.debug("Payment failure.");
        return false;
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        throw new NotImplimentedMethod("for " + withdrawMerchantOperationDto);
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        throw new NotImplimentedMethod("for " + request);
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        throw new NotImplimentedMethod("for " + params);
    }

    @Override
    public boolean isValidDestinationAddress(String address) {
        return withdrawUtils.isValidDestinationAddress(address);
    }
}
