package com.exrates.inout.service.merchant.impl;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Transaction;
import com.exrates.inout.exceptions.NotImplimentedMethod;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.service.AlgorithmService;
import com.exrates.inout.service.TransactionService;
import com.exrates.inout.service.merchant.Privat24Service;
import com.exrates.inout.service.utils.WithdrawUtils;
import com.squareup.okhttp.OkHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class Privat24ServiceImpl implements Privat24Service {

    private static final Logger LOGGER = LogManager.getLogger("merchant");

    private final OkHttpClient client = new OkHttpClient();

    @Autowired
    private CryptoCurrencyProperties ccp;
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

        Map<String, String> properties = new HashMap<>();

        properties.put("amt", String.valueOf(amountToPay));
        properties.put("ccy", creditsOperation.getCurrency().getName());
        properties.put("merchant", ccp.getPaymentSystemMerchants().getPrivat24().getMerchant());
        properties.put("order", String.valueOf(transaction.getId()));
        properties.put("details", ccp.getPaymentSystemMerchants().getPrivat24().getDetails());
        properties.put("ext_details", ccp.getPaymentSystemMerchants().getPrivat24().getExtDetails() + transaction.getId());
        properties.put("pay_way", ccp.getPaymentSystemMerchants().getPrivat24().getPayWay());
        properties.put("return_url", ccp.getPaymentSystemMerchants().getPrivat24().getReturnUrl());
        properties.put("server_url", ccp.getPaymentSystemMerchants().getPrivat24().getServerUrl());

        return properties;
    }

    @Override
    @Transactional
    public boolean confirmPayment(Map<String, String> params, String signature, String payment) {
        Transaction transaction;
        try {
            transaction = transactionService.findById(Integer.parseInt(params.get("order")));
            if (transaction.isProvided()) {
                return true;
            }
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error(e);
            return false;
        }
        Double transactionSum = transaction.getAmount().add(transaction.getCommissionAmount()).doubleValue();

        String checkSignature = algorithmService.sha1(algorithmService.computeMD5Hash(payment + ccp.getPaymentSystemMerchants().getPrivat24().getPassword()));
        if (checkSignature.equals(signature)
                && Double.parseDouble(params.get("amt")) == transactionSum) {
            transactionService.provideTransaction(transaction);
            return true;
        }
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