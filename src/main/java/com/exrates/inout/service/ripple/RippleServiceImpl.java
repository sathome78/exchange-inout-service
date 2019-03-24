package com.exrates.inout.service.ripple;

import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.CheckDestinationTagException;
import com.exrates.inout.exceptions.MerchantInternalException;
import com.exrates.inout.exceptions.WithdrawRequestPostException;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.GtagService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.util.WithdrawUtils;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;


//exrates.model.Merchant;
//exrates.model.dto.RefillRequestAcceptDto;
//exrates.model.dto.RefillRequestCreateDto;
//exrates.model.dto.WithdrawMerchantOperationDto;
//exrates.service.CurrencyService;
//exrates.service.GtagService;
//exrates.service.MerchantService;
//exrates.service.RefillService;
//exrates.service.exception.CheckDestinationTagException;
//exrates.service.exception.MerchantInternalException;
//exrates.service.exception.WithdrawRequestPostException;
//exrates.service.util.WithdrawUtils;

/**
 * Created by maks on 11.05.2017.
 */
@Log4j2(topic = "ripple_log")
@Service
@PropertySource("classpath:/merchants/ripple.properties")
public class RippleServiceImpl implements RippleService {

    private @Value("${ripple.account.address}")
    String systemAddress;

    @Autowired
    private RippleTransactionService rippleTransactionService;
    @Autowired
    private RippledNodeService rippledNodeService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RefillService refillService;
    @Autowired
    private WithdrawUtils withdrawUtils;
    @Autowired
    private GtagService gtagService;

    private static final String XRP_MERCHANT = "Ripple";

    private static final int MAX_TAG_DESTINATION_DIGITS = 9;

    private static final String DESTINATION_TAG_ERR_MSG = "message.ripple.tagError";

    private Currency currency;

    private Merchant merchant;

    @PostConstruct
    private void init() {
        currency = currencyService.findByName("XRP");
        merchant = merchantService.findByName(XRP_MERCHANT);
    }


    /*method for admin manual check transaction by hash*//*
  @Override
  public void manualCheckNotReceivedTransaction(String hash) {
    JSONObject response = rippledNodeService.getTransaction(hash);
    onTransactionReceive(response);
  }*/


    /*return: true if tx validated; false if not validated but validation in process,
    throws Exception if declined*/
    @Override
    public boolean checkSendedTransaction(String hash, String additionalParams) {
        return rippleTransactionService.checkSendedTransactionConsensus(hash, additionalParams);
    }

    @Override
    public void onTransactionReceive(String hash, Integer destinationTag, String amount) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("hash", hash);
        paramsMap.put("address", String.valueOf(destinationTag));
        paramsMap.put("amount", amount);
        this.processPayment(paramsMap);
    }

    private boolean checkTransactionForDuplicate(String hash) {
        return StringUtils.isEmpty(hash) || refillService.getRequestIdByMerchantIdAndCurrencyIdAndHash(merchant.getId(), currency.getId(), hash).isPresent();
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
        if (!"XRP".equalsIgnoreCase(withdrawMerchantOperationDto.getCurrency())) {
            throw new WithdrawRequestPostException("Currency not supported by merchant");
        }
        return rippleTransactionService.withdraw(withdrawMerchantOperationDto);
    }

    /*generate 9 digits(Unsigned Integer) for identifying payment */
    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        Integer destinationTag = generateUniqDestinationTag(request.getUserId());
        String message = messageSource.getMessage("merchants.refill.xrp",
                new String[]{systemAddress, destinationTag.toString()}, request.getLocale());
        DecimalFormat myFormatter = new DecimalFormat("###.##");
        return new HashMap<String, String>() {{
            put("address", myFormatter.format(destinationTag));
            put("message", message);
        }};
    }

    @Synchronized
    @Override
    public void processPayment(Map<String, String> params) {
        String address = params.get("address");
        String hash = params.get("hash");
        if (checkTransactionForDuplicate(hash)) {
            log.warn("*** XRP *** transaction {} already accepted", hash);
            return;
        }
        Currency currency = currencyService.findByName("XRP");
        Merchant merchant = merchantService.findByName(XRP_MERCHANT);
        BigDecimal amount = rippleTransactionService.normalizeAmountToDecimal(params.get("amount"));

        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .address(address)
                .merchantId(merchant.getId())
                .currencyId(currency.getId())
                .amount(amount)
                .merchantTransactionId(hash)
                .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
                .build();

        int requestId = refillService.createAndAutoAcceptRefillRequest(requestAcceptDto);

        final String username = refillService.getUsernameByRequestId(requestId);

        log.debug("Process of sending data to Google Analytics...");
        gtagService.sendGtagEvents(amount.toString(), currency.getName(), username);
    }

    @Override
    public String getMainAddress() {
        return systemAddress;
    }

    private Integer generateUniqDestinationTag(int userId) {
        Currency currency = currencyService.findByName("XRP");
        Merchant merchant = merchantService.findByName(XRP_MERCHANT);
        Optional<Integer> id;
        int destinationTag;
        do {
            destinationTag = generateDestinationTag(userId);
            id = refillService.getRequestIdReadyForAutoAcceptByAddressAndMerchantIdAndCurrencyId(String.valueOf(destinationTag),
                    currency.getId(), merchant.getId());
        } while (id.isPresent());
        return destinationTag;
    }

    private Integer generateDestinationTag(int userId) {
        String idInString = String.valueOf(userId);
        int randomNumberLength = MAX_TAG_DESTINATION_DIGITS - idInString.length();
        if (randomNumberLength < 0) {
            throw new MerchantInternalException("error generating new destination tag for ripple" + userId);
        }
        String randomIntInstring = String.valueOf(100000000 + new Random().nextInt(100000000));
        return Integer.valueOf(idInString.concat(randomIntInstring.substring(0, randomNumberLength)));
    }

    //TODO remove after changes in mobile api
    @Override
    public String getPaymentMessage(String additionalTag, Locale locale) {
        return messageSource.getMessage("merchants.refill.xrp",
                new String[]{systemAddress, additionalTag}, locale);
    }

    /*must bee only 32 bit number = 0 - 4294967295*/
    @Override
    public void checkDestinationTag(String destinationTag) throws CheckDestinationTagException {
        if (!(org.apache.commons.lang.math.NumberUtils.isDigits(destinationTag)
                && Long.valueOf(destinationTag) <= 4294967295L)) {
            throw new CheckDestinationTagException(DESTINATION_TAG_ERR_MSG, additionalWithdrawFieldName());
        }
    }

    @Override
    public boolean isValidDestinationAddress(String address) {

        return withdrawUtils.isValidDestinationAddress(systemAddress, address);
    }
}
