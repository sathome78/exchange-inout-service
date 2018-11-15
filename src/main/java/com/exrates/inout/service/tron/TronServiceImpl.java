package com.exrates.inout.service.tron;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.utils.WithdrawUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2(topic = "tron")
@Service
public class TronServiceImpl implements TronService {

    private final TronNodeService tronNodeService;
    private final RefillService refillService;
    private final CurrencyService currencyService;
    private final MerchantService merchantService;
    private final MessageSource messageSource;
    private final WithdrawUtils withdrawUtils;


    private final static String CURRENCY_NAME = "TRX";
    private final static String MERCHANT_NAME = "TRX";
    private int merchantId;
    private int currencyId;

    private Set<String> addressesHEX = Collections.synchronizedSet(new HashSet<>());

    @Autowired
    public TronServiceImpl(TronNodeService tronNodeService, RefillService refillService, CurrencyService currencyService, MerchantService merchantService, WithdrawUtils withdrawUtils, MessageSource messageSource) {
        this.tronNodeService = tronNodeService;
        this.refillService = refillService;
        this.currencyService = currencyService;
        this.merchantService = merchantService;
        this.messageSource = messageSource;
        this.withdrawUtils = withdrawUtils;
    }

    @PostConstruct
    private void init() {
        merchantId = merchantService.findByName(MERCHANT_NAME).getId();
        currencyId = currencyService.findByName(CURRENCY_NAME).getId();
        addressesHEX.addAll(refillService.findAddressDtos(merchantId, currencyId).stream().map(RefillRequestAddressDto::getPubKey).collect(Collectors.toList()));
    }


    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        TronNewAddressDto dto = tronNodeService.getNewAddress();
        String message = messageSource.getMessage("merchants.refill.btc",
                new Object[]{dto.getAddress()}, request.getLocale());
        addressesHEX.add(dto.getHexAddress());
        return new HashMap<String, String>() {{
            put("address",  dto.getAddress());
            put("privKey", dto.getPrivateKey());
            put("pubKey", dto.getHexAddress());
            put("message", message);
            put("qr", dto.getAddress());
        }};
    }

    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        String address = params.get("address");
        String hash = params.get("hash");
        Currency currency = currencyService.findByName(CURRENCY_NAME);
        Merchant merchant = merchantService.findByName(MERCHANT_NAME);
        BigDecimal amount = new BigDecimal(params.get("amount"));
        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .address(address)
                .merchantId(merchant.getId())
                .currencyId(currency.getId())
                .amount(amount)
                .merchantTransactionId(hash)
                .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
                .build();
        refillService.autoAcceptRefillRequest(requestAcceptDto);
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        throw new RuntimeException("Tron withdraw method not implemented!");
    }

    @Override
    public int getMerchantId() {
        return merchantId;
    }

    @Override
    public int getCurrencyId() {
        return currencyId;
    }

    private boolean isTransactionDuplicate(String hash, int currencyId, int merchantId) {
        return StringUtils.isEmpty(hash)
                || refillService.getRequestIdByMerchantIdAndCurrencyIdAndHash(merchantId, currencyId, hash).isPresent();
    }

    @Override
    public BigDecimal countSpecCommission(BigDecimal amount, String destinationTag, Integer merchantId) {
        return new BigDecimal(0.1).setScale(3, RoundingMode.HALF_UP);
    }

    @Override
    public boolean isValidDestinationAddress(String address) {

        return withdrawUtils.isValidDestinationAddress(address);
    }
}
