package com.exrates.inout.service.stellar;

import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.stellar.sdk.Memo;
import org.stellar.sdk.MemoId;
import org.stellar.sdk.MemoText;
import org.stellar.sdk.responses.TransactionResponse;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

@Log4j2(topic = "stellar_log")
@Service
public class StellarServiceImpl implements StellarService {

    private @Value("${stellar.horizon.url}")
    String SEVER_URL;
    private @Value("${stellar.account.name}")
    String ACCOUNT_NAME;
    private @Value("${stellar.account.seed}")
    String ACCOUNT_SECRET;

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RefillService refillService;
    @Autowired
    private StellarTransactionService stellarTransactionService;

    private Merchant merchant;
    private Currency currency;
    private static final String DESTINATION_TAG_ERR_MSG = "message.stellar.tagError";

    @PostConstruct
    public void init() {
        currency = currencyService.findByName("XLM");
        merchant = merchantService.findByName(XLM_MERCHANT);
    }


    private static final String XLM_MERCHANT = "Stellar";

    private static final int MAX_TAG_DESTINATION_DIGITS = 9;

    @Override
    public void manualCheckNotReceivedTransaction(String hash) {

    }

    @Override
    public boolean checkSendedTransaction(String hash, String additionalParams) {
        return false;
    }

    @Transactional
    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
        if (!"XLM".equalsIgnoreCase(withdrawMerchantOperationDto.getCurrency())) {
            throw new WithdrawRequestPostException("Currency not supported by merchant");
        }
        return stellarTransactionService.withdraw(withdrawMerchantOperationDto, SEVER_URL, ACCOUNT_SECRET);
    }

    @Synchronized
    @Override
    public void onTransactionReceive(TransactionResponse payment, String amount, String currencyName, String merchant) {
        log.debug("income transaction {} ", payment.getMemo() + " " + amount);
        if (checkTransactionForDuplicate(payment)) {
            try {
                throw new DuplicatedMerchantTransactionIdOrAttemptToRewriteException(payment.getHash());
            } catch (DuplicatedMerchantTransactionIdOrAttemptToRewriteException e) {
                log.warn("xlm transaction {} allready accepted", payment.getHash());
                return;
            }
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("hash", payment.getHash());
        String memo = defineAndGetMemo(payment.getMemo());
        if(memo == null) {
            log.warn("memo is null");
            return;
        }
        paramsMap.put("currency", currencyName);
        paramsMap.put("merchant", merchant);
        paramsMap.put("address", memo);
        paramsMap.put("amount", amount);
        try {
            this.processPayment(paramsMap);
        } catch (RefillRequestAppropriateNotFoundException e) {
            log.error("xlm refill address not found {}", payment);
        }
    }

    private String defineAndGetMemo(Memo memo) {
        String parsedMemo = null;
        if (memo instanceof MemoText) {
            parsedMemo = ((MemoText) memo).getText();
        } else if (memo instanceof MemoId) {
            Long memoL = ((MemoId) memo).getId();
            parsedMemo = memoL.toString();
        }
        return parsedMemo == null ? null : parsedMemo.replaceAll(" ", "").replaceAll("\\,", "");
    }

    @Transactional
    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        Integer destinationTag = generateUniqDestinationTag(request.getUserId());
        String message = messageSource.getMessage("merchants.refill.xlm",
                new Object[]{ACCOUNT_NAME, destinationTag}, request.getLocale());
        DecimalFormat myFormatter = new DecimalFormat("###.##");
        return new HashMap<String, String>() {{
            put("address",  String.valueOf(destinationTag));
            put("message", message);
            put("qr", ACCOUNT_NAME);
        }};
    }

    private boolean checkTransactionForDuplicate(TransactionResponse payment) {
        return StringUtils.isEmpty(payment.getHash()) || refillService.getRequestIdByMerchantIdAndCurrencyIdAndHash(merchant.getId(), currency.getId(),
                                                                            payment.getHash()).isPresent();
    }

    private Integer generateUniqDestinationTag(int userId) {
        Optional<Integer> id = null;
        int destinationTag;
        do {
            destinationTag = generateDestinationTag(userId);
            id = refillService.getRequestIdReadyForAutoAcceptByAddressAndMerchantIdAndCurrencyId(String.valueOf(destinationTag),
                    currency.getId(), merchant.getId());
        } while (id.isPresent());
        log.debug("tag is {}", destinationTag);
        return destinationTag;
    }

    private Integer generateDestinationTag(int userId) {
        String idInString = String.valueOf(userId);
        int randomNumberLength = MAX_TAG_DESTINATION_DIGITS - idInString.length();
        if (randomNumberLength < 0) {
            throw new MerchantInternalException("error generating new destination tag for stellar" + userId);
        }
        String randomIntInstring = String.valueOf(100000000 + new Random().nextInt(100000000));
        return Integer.valueOf(idInString.concat(randomIntInstring.substring(0, randomNumberLength)));
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        String address = params.get("address");
        String hash = params.get("hash");
        com.exrates.inout.domain.main.Currency currency = currencyService.findByName(params.get("currency"));
        Merchant merchant = merchantService.findByName(params.get("merchant"));
        BigDecimal amount = new BigDecimal(params.get("amount"));
        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .address(address)
                .merchantId(merchant.getId())
                .currencyId(currency.getId())
                .amount(amount)
                .merchantTransactionId(hash)
                .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
                .build();
        try {
            refillService.autoAcceptRefillRequest(requestAcceptDto);
        } catch (RefillRequestAppropriateNotFoundException e) {
            log.debug("RefillRequestNotFountException: " + params);
            Integer requestId = refillService.createRefillRequestByFact(requestAcceptDto);
            requestAcceptDto.setRequestId(requestId);
            refillService.autoAcceptRefillRequest(requestAcceptDto);
        }
    }

    @Override
    public String getMainAddress() {
        return ACCOUNT_NAME;
    }
  //TODO remove after changes in mobile api
    @Override
    public String getPaymentMessage(String additionalTag, Locale locale) {
        return messageSource.getMessage("merchants.refill.xlm",
                new Object[]{ACCOUNT_NAME, additionalTag}, locale);
    }


    /*must bee only unsigned int = Memo.id - unsigned 64-bit number, MAX_SAFE_INTEGER  memo 0 - 9007199254740991*/
    @Override
    public void checkDestinationTag(String destinationTag) {
        /*if (!(org.apache.commons.lang.math.NumberUtils.isDigits(destinationTag)
                && Long.valueOf(destinationTag) <= 9007199254740991L)) {
            throw new CheckDestinationTagException(DESTINATION_TAG_ERR_MSG, this.additionalWithdrawFieldName());
        }*/
        if (destinationTag.length() > 26) {
            throw new CheckDestinationTagException(DESTINATION_TAG_ERR_MSG, this.additionalWithdrawFieldName());
        }
    }

    @Override
    public BigDecimal countSpecCommission(BigDecimal amount, String destinationTag, Integer merchantId) {
        Merchant merchant = merchantService.findById(merchantId);
        switch (merchant.getName()) {
            case "Stellar" : {
                return new BigDecimal(0.001).setScale(5, RoundingMode.HALF_UP);
            }
            case "SLT" : {
                return new BigDecimal(1).setScale(5, RoundingMode.HALF_UP);
            }
            case "VNT" : {
                return new BigDecimal(1).setScale(5, RoundingMode.HALF_UP);
            }
            case "TERN" : {
                return new BigDecimal(1).setScale(5, RoundingMode.HALF_UP);
            }
            default:
                return new BigDecimal(0.1).setScale(5, RoundingMode.HALF_UP);
        }

    }
}
