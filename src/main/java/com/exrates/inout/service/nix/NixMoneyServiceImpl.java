package com.exrates.inout.service.nix;

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
import com.exrates.inout.properties.models.NixMoneyProperty;
import com.exrates.inout.service.*;
import com.exrates.inout.util.WithdrawUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

@Service
public class NixMoneyServiceImpl implements NixMoneyService {

    private static final Logger logger = LogManager.getLogger(NixMoneyServiceImpl.class);

    private String url;
    private String payeeAccountUSD;
    private String payeeAccountEUR;
    private String payeeName;
    private String payeePassword;
    private String paymentUrl;
    private String noPaymentUrl;
    private String statustUrl;

    private AlgorithmService algorithmService;
    private RefillRequestDao refillRequestDao;
    private MerchantService merchantService;
    private CurrencyService currencyService;
    private RefillService refillService;
    private WithdrawUtils withdrawUtils;
    private GtagService gtagService;

    @Autowired
    public NixMoneyServiceImpl(AlgorithmService algorithmService, RefillRequestDao refillRequestDao,
                               MerchantService merchantService, CurrencyService currencyService, RefillService refillService,
                               WithdrawUtils withdrawUtils, GtagService gtagService, CryptoCurrencyProperties cryptoCurrencyProperties){
        this.algorithmService = algorithmService;
        this.refillRequestDao = refillRequestDao;
        this.merchantService = merchantService;
        this.currencyService = currencyService;
        this.refillService = refillService;
        this.withdrawUtils = withdrawUtils;
        this.gtagService = gtagService;

        NixMoneyProperty nixMoneyProperty = cryptoCurrencyProperties.getPaymentSystemMerchants().getNixmoney();
        this.url = nixMoneyProperty.getUrl();
        this.payeeAccountUSD = nixMoneyProperty.getPayeeAccountUSD();
        this.payeeAccountEUR = nixMoneyProperty.getPayeeAccountEUR();
        this.payeeName = nixMoneyProperty.getPayeeName();
        this.payeePassword = nixMoneyProperty.getPayeePassword();
        this.paymentUrl = nixMoneyProperty.getPaymentUrl();
        this.noPaymentUrl = nixMoneyProperty.getNoPaymentUrl();
        this.statustUrl = nixMoneyProperty.getStatustUrl();
    }

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
                put("PAYEE_ACCOUNT", payeeAccountUSD);
            }
            if (currency.equals("EUR")) {
                put("PAYEE_ACCOUNT", payeeAccountEUR);
            }
            put("PAYMENT_ID", requestId);
            put("PAYEE_NAME", payeeName);
            put("PAYMENT_AMOUNT", amountToPay);
            put("PAYMENT_URL", paymentUrl);
            put("NOPAYMENT_URL", noPaymentUrl);
            put("BAGGAGE_FIELDS", "PAYEE_ACCOUNT PAYMENT_AMOUNT PAYMENT_ID");
            put("STATUS_URL", statustUrl);
        }};
        /**/
        return generateFullUrlMap(url, "POST", properties);
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

        String passwordMD5 = algorithmService.computeMD5Hash(payeePassword).toUpperCase();
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

            final String username = refillService.getUsernameByRequestId(requestId);

            logger.debug("Process of sending data to Google Analytics...");
            gtagService.sendGtagEvents(amount.toString(), currency.getName(), username);
        }
    }

    @Override
    public boolean isValidDestinationAddress(String address) {

        return withdrawUtils.isValidDestinationAddress(address);
    }

}
