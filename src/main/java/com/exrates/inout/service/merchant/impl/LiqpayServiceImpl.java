package com.exrates.inout.service.merchant.impl;


import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.exceptions.NotImplimentedMethod;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.service.AlgorithmService;
import com.exrates.inout.service.TransactionService;
import com.exrates.inout.service.merchant.LiqpayService;
import com.exrates.inout.service.utils.WithdrawUtils;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;

import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class LiqpayServiceImpl implements LiqpayService {

    private static final Logger LOGGER = LogManager.getLogger(AdvcashServiceImpl.class);

    @Autowired
    private CryptoCurrencyProperties ccp;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AlgorithmService algorithmService;
    @Autowired
    private WithdrawUtils withdrawUtils;

    @Transactional
    public RedirectView preparePayment(CreditsOperation creditsOperation, String email) {
    /*Transaction transaction = transactionService.createTransactionRequest(creditsOperation);

    BigDecimal sum = transaction.getAmount().add(transaction.getCommissionAmount());
    final String currency = transaction.getCurrency().getName();
    final Number amountToPay = sum.setScale(2, BigDecimal.ROUND_HALF_UP);

    Map params = new HashMap();
    params.put("version", Integer.parseInt(apiVersion));
    params.put("public_key", public_key);
    params.put("action", action);
    params.put("amount", amountToPay);
    params.put("currency", creditsOperation.getCurrency().getName());
    params.put("description", "Order: " + transaction.getId());
    params.put("order_id", transaction.getId());
    byte[] hashSha1 = sha1(transaction.getId() + private_key);
    String hash = base64_encode(hashSha1);
    params.put("info", hash);


    Gson gson = new Gson();
    String jsonData = gson.toJson(params);
    String data = algorithmService.base64Encode(jsonData);
    byte[] signatureSha1 = sha1((private_key + data + private_key));
    String signature = base64_encode(signatureSha1);

    final PendingPayment payment = new PendingPayment();
    payment.setTransactionHash(hash);
    payment.setInvoiceId(transaction.getId());
    pendingPaymentDao.create(payment);


    Properties properties = new Properties();
    properties.put("data", data);
    properties.put("signature", signature);

    RedirectView redirectView = new RedirectView(url);
    redirectView.setAttributes(properties);


    return redirectView;*/

        return null;

    }

    private static String base64_encode(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }

    private static byte[] sha1(String param) {
        try {
            MessageDigest e = MessageDigest.getInstance("SHA-1");
            e.reset();
            e.update(param.getBytes(StandardCharsets.UTF_8));
            return e.digest();
        } catch (Exception var2) {
            throw new RuntimeException("Can\'t calc SHA-1 hash", var2);
        }
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
        BigDecimal amountToPay = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
        /**/
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("version", ccp.getPaymentSystemMerchants().getLiqpay().getApiVersion());
            put("public_key", ccp.getPaymentSystemMerchants().getLiqpay().getPublicKey());
            put("action", ccp.getPaymentSystemMerchants().getLiqpay().getAction());
            put("amount", amountToPay);
            put("currency", currency);
            put("description", "Order: " + orderId);
            put("order_id", orderId);
            byte[] hashSha1 = sha1(orderId + ccp.getPaymentSystemMerchants().getLiqpay().getPrivateKey());
            String hash = base64_encode(hashSha1);
            put("info", hash);
        }};
        /**/
        Gson gson = new Gson();
        String jsonData = gson.toJson(params);
        String data = algorithmService.base64Encode(jsonData);
        byte[] signatureSha1 = sha1((ccp.getPaymentSystemMerchants().getLiqpay().getPrivateKey() + data + ccp.getPaymentSystemMerchants().getLiqpay().getPrivateKey()));
        String signature = base64_encode(signatureSha1);
        /**/
        Properties properties = new Properties();
        properties.put("data", data);
        properties.put("signature", signature);
        /**/
        String fullUrl = generateFullUrl(ccp.getPaymentSystemMerchants().getLiqpay().getUrl(), properties);
        return new HashMap<String, String>() {{
            put("$__redirectionUrl", fullUrl);
        }};
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
