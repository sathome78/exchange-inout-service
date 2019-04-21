package com.exrates.inout.service.impl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



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
import com.exrates.inout.service.*;
import com.exrates.inout.util.WithdrawUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

@Service
@PropertySource("classpath:/merchants/advcashmoney.properties")
//@Log4j2
public class AdvcashServiceImpl implements AdvcashService {

   private static final Logger log = LogManager.getLogger(AdvcashServiceImpl.class);


    private @Value("${advcash.url}")
    String url;
    private @Value("${advcash.accountId}")
    String accountId;
    private @Value("${advcash.payeeName}")
    String payeeName;
    private @Value("${advcash.paymentSuccess}")
    String paymentSuccess;
    private @Value("${advcash.paymentFailure}")
    String paymentFailure;
    private @Value("${advcash.paymentStatus}")
    String paymentStatus;
    private @Value("${advcash.USDAccount}")
    String usdCompanyAccount;
    private @Value("${advcash.EURAccount}")
    String eurCompanyAccount;
    private @Value("${advcash.payeePassword}")
    String payeePassword;

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
    @Autowired
    private GtagService gtagService;

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        throw new NotImplimentedMethod("for " + withdrawMerchantOperationDto);
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) throws RefillRequestIdNeededException {
        Integer requestId = request.getId();
        if (requestId == null) {
            throw new RefillRequestIdNeededException(request.toString());
        }
        BigDecimal sum = request.getAmount();
        String currency = request.getCurrencyName();
        BigDecimal amountToPay = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
        /**/
        Properties properties = new Properties() {{
            put("ac_account_email", accountId);
            put("ac_sci_name", payeeName);
            put("ac_amount", amountToPay);
            put("ac_currency", currency);
            put("ac_order_id", requestId);
            String sign = accountId + ":" + payeeName + ":" + amountToPay
                    + ":" + currency + ":" + payeePassword
                    + ":" + requestId;
            String transactionHash = algorithmService.sha256(sign);
            put("ac_sign", transactionHash);
            put("transaction_hash", transactionHash);
            put("ac_success_url", paymentSuccess);
            put("ac_success__method", "POST");
        }};
        /**/
        return generateFullUrlMap(url, "POST", properties, properties.getProperty("ac_sign"));
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        Integer requestId = Integer.valueOf(params.get("ac_order_id"));
        String merchantTransactionId = params.get("ac_transfer");
        Currency currency = currencyService.findByName(params.get("ac_merchant_currency"));
        Merchant merchant = merchantService.findByName("Advcash Money");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(params.get("ac_amount"))).setScale(9);

        RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));

        if (params.get("ac_transaction_status").equals("COMPLETED")
                && refillRequest.getMerchantRequestSign().equals(params.get("transaction_hash"))
                && refillRequest.getAmount().equals(amount)) {
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

            log.debug("Process of sending data to Google Analytics...");
            gtagService.sendGtagEvents(amount.toString(), currency.getName(), username);
        }
    }

    @Override
    public boolean isValidDestinationAddress(String address) {

        return withdrawUtils.isValidDestinationAddress(address);
    }

}
