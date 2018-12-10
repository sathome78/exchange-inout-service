package com.exrates.inout.service.autist;

import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.RefillRequestPutOnBchExamDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.MerchantInternalException;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.exrates.inout.service.autist.MemoDecryptor.decryptBTSmemo;

@Service("aunitServiceImpl")
@Log4j2(topic = "aunit")
public class AunitServiceImpl implements AunitService {

    static final String AUNIT_CURRENCY = "AUNIT";
    static final String AUNIT_MERCHANT = "AUNIT";
    private static final int MAX_TAG_DESTINATION_DIGITS = 9;

    @Value("${aunit.main-address}")
    private String systemAddress;

    private final Merchant merchant;
    private final Currency currency;

    private final MessageSource messageSource;
    private final RefillService refillService;

    @Autowired
    public AunitServiceImpl(MerchantService merchantService,
                            CurrencyService currencyService,
                            MessageSource messageSource,
                            RefillService refillService) {
        this.messageSource = messageSource;
        this.refillService = refillService;
        currency = currencyService.findByName(AUNIT_CURRENCY);
        merchant = merchantService.findByName(AUNIT_MERCHANT);
    }

    @Override
    public Merchant getMerchant() {
        return merchant;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        Integer destinationTag = generateUniqDestinationTag(request.getUserId());
        String message = messageSource.getMessage("merchants.refill.xrp",
                new String[]{systemAddress, destinationTag.toString()}, request.getLocale());
        return new HashMap<String, String>() {{
            put("address", String.valueOf(destinationTag));
            put("message", message);
            put("qr", systemAddress);
        }};
    }

    private Integer generateUniqDestinationTag(int userId) {
        Optional<Integer> id;
        int destinationTag;
        do {
            destinationTag = generateDestinationTag(userId);
            id = refillService.getRequestIdReadyForAutoAcceptByAddressAndMerchantIdAndCurrencyId(String.valueOf(destinationTag), //wtf
                    currency.getId(), merchant.getId());
        } while (id.isPresent());
        return destinationTag;
    }

    private Integer generateDestinationTag(int userId) {
        String idInString = String.valueOf(userId);
        int randomNumberLength = MAX_TAG_DESTINATION_DIGITS - idInString.length();
        if (randomNumberLength < 0) {
            throw new MerchantInternalException("error generating new destination tag for aunit" + userId);
        }
        String randomIntInstring = String.valueOf(100000000 + new Random().nextInt(100000000));
        return Integer.valueOf(idInString.concat(randomIntInstring.substring(0, randomNumberLength)));
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        String address = params.get("address");
        String hash = params.get("hash");
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
            setIdAndAccept(requestAcceptDto);
        }
    }

    private void setIdAndAccept(RefillRequestAcceptDto requestAcceptDto) throws RefillRequestAppropriateNotFoundException {
        try{
            Integer requestId = refillService.createRefillRequestByFact(requestAcceptDto);
            requestAcceptDto.setRequestId(requestId);
            refillService.autoAcceptRefillRequest(requestAcceptDto);
        } catch (Exception e){
            log.error(e);
            throw e;
        }
    }

    @Override
    public RefillRequestAcceptDto createRequest(String hash, String address, BigDecimal amount) {
        if (isTransactionDuplicate(hash, currency.getId(), merchant.getId())) {
            log.error("aunit transaction allready received!!! {}", hash);
            throw new RuntimeException("aunit transaction allready received!!!");
        }
        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .address(address)
                .merchantId(merchant.getId())
                .currencyId(currency.getId())
                .amount(amount)
                .merchantTransactionId(hash)
                .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
                .build();
        Integer requestId = refillService.createRefillRequestByFact(requestAcceptDto);
        requestAcceptDto.setRequestId(requestId);
        return requestAcceptDto;
    }

    private boolean isTransactionDuplicate(String hash, int currencyId, int merchantId) {
        return StringUtils.isEmpty(hash)
                || refillService.getRequestIdByMerchantIdAndCurrencyIdAndHash(merchantId, currencyId, hash).isPresent();
    }

    @Override
    public void putOnBchExam(RefillRequestAcceptDto requestAcceptDto) {
        try {
            refillService.putOnBchExamRefillRequest(
                    RefillRequestPutOnBchExamDto.builder()
                            .requestId(requestAcceptDto.getRequestId())
                            .merchantId(merchant.getId())
                            .currencyId(currency.getId())
                            .address(requestAcceptDto.getAddress())
                            .amount(requestAcceptDto.getAmount())
                            .hash(requestAcceptDto.getMerchantTransactionId())
                            .build());
        } catch (RefillRequestAppropriateNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public String getMainAddress() {
        return systemAddress;
    }
}
