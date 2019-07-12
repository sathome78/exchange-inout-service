package com.exrates.inout.service.tron;

import com.exrates.inout.domain.dto.RefillRequestAddressDto;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.domain.dto.TronReceivedTransactionDto;
import com.exrates.inout.domain.dto.TronTransferDto;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.OtherTronProperty;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.bitshares.memo.Preconditions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

//@Log4j2(topic = "tron")
@Service
public class TronTransactionsServiceImpl implements TronTransactionsService {

    private static final Logger log = LogManager.getLogger("tron");

    private final TronNodeService tronNodeService;
    private final TronService tronService;
    private final RefillService refillService;
    private final TronTokenContext tronTokenContext;

    private String MAIN_ADDRESS_HEX;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledExecutorService transferScheduler = Executors.newScheduledThreadPool(3);

    @Autowired
    public TronTransactionsServiceImpl(TronNodeService tronNodeService, TronService tronService,
                                       RefillService refillService, TronTokenContext tronTokenContext,
                                       CryptoCurrencyProperties cryptoCurrencyProperties) {
        this.tronNodeService = tronNodeService;
        this.tronService = tronService;
        this.refillService = refillService;
        this.tronTokenContext = tronTokenContext;

        OtherTronProperty tronProperty = cryptoCurrencyProperties.getOtherCoins().getTron();
        this.MAIN_ADDRESS_HEX = tronProperty.getMainAccountHexAddress();
    }

    @PostConstruct
    private void init() {
        scheduler.scheduleAtFixedRate(this::checkUnconfirmedJob, 5, 2, TimeUnit.MINUTES);
        transferScheduler.scheduleAtFixedRate(this::transferToMainAccountJob, 5, 30, TimeUnit.MINUTES);
        transferScheduler.scheduleAtFixedRate(this::transferTokensToMainAccountJob, 5, 30, TimeUnit.MINUTES);
    }

    private void checkUnconfirmedJob() {
        List<RefillRequestFlatDto> dtos = refillService.getInExamineWithChildTokensByMerchantIdAndCurrencyIdList(tronService.getMerchantId(), tronService.getCurrencyId());
        dtos.forEach(p->{
            try {
                if (checkIsTransactionConfirmed(p.getMerchantTransactionId())) {
                    processTransaction(p.getId(), p.getAddress(), p.getMerchantTransactionId(), p.getAmount().toString(), p.getMerchantId(), p.getCurrencyId());
                }
            } catch (Exception e) {
                log.error(e);
            }
        });

    }

    private void transferToMainAccountJob() {
        List<RefillRequestAddressDto> listRefillRequestAddressDto = refillService.findAllAddressesNeededToTransfer(tronService.getMerchantId(), tronService.getCurrencyId());
        listRefillRequestAddressDto.forEach(p->{
            try {
                transferToMainAccount(p);
                refillService.updateAddressNeedTransfer(p.getAddress(), tronService.getMerchantId(), tronService.getCurrencyId(), false);
            } catch (Exception e) {
                log.error(e);
            }
        });
    }

    private void transferTokensToMainAccountJob() {
        List<TronTrc10Token> tokensList = tronTokenContext.getAll();
        List<RefillRequestAddressDto> listRefillRequestAddressDto = new ArrayList<>();
        tokensList.forEach(p -> listRefillRequestAddressDto.addAll(refillService.findAllAddressesNeededToTransfer(p.getMerchantId(), p.getCurrencyId())));
        listRefillRequestAddressDto.forEach(p->{
            try {
                TronTrc10Token token = tronTokenContext.getByCurrencyId(p.getCurrencyId());
                transferTokenToMainAccount(p, token.getNameDescription(), token.getBlockchainName());
                refillService.updateAddressNeedTransfer(p.getAddress(), p.getMerchantId(), p.getCurrencyId(), false);
            } catch (Exception e) {
                log.error(e);
            }
        });
    }

    private void transferToMainAccount(RefillRequestAddressDto dto) {
        Long accountAmount = tronNodeService.getAccount(dto.getPubKey()).getLong("balance");
        log.debug("balance {} {}", dto.getAddress(), accountAmount);
        easyTransferByPrivate(dto.getPrivKey(), MAIN_ADDRESS_HEX, accountAmount);
    }

    private void transferTokenToMainAccount(RefillRequestAddressDto dto, String tokenName, String tokenBchName) {
        JSONArray tokensBalances = tronNodeService.getAccount(dto.getPubKey()).getJSONArray("assetV2");
        long balance = StreamSupport.stream(tokensBalances.spliterator(), true)
                                    .map(JSONObject.class::cast)
                                    .filter(p -> p.getString("key").equals(tokenBchName))
                                    .findFirst()
                                    .map(p -> p.getLong("value"))
                                    .orElseThrow(() -> new RuntimeException("token balance not found"));
        log.debug("balance {} {} {}", dto.getAddress(), balance, tokenBchName);
        easyTransferAssetByPrivate(dto.getPrivKey(), MAIN_ADDRESS_HEX, balance, tokenName);
    }

    @Override
    public boolean checkIsTransactionConfirmed(String txHash) {
        JSONObject rawResponse = tronNodeService.getTransaction(txHash);
        return rawResponse.getBoolean("confirmed");
    }

    @Override
    public void processTransaction(TronReceivedTransactionDto p) {
        processTransaction(p.getId(), p.getAddressBase58(), p.getHash(), p.getAmount(), p.getMerchantId(), p.getCurrencyId());
    }

    @Override
    public void processTransaction(int id, String address, String hash, String amount, Integer merchantId, Integer currencyId) {
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        map.put("hash", hash);
        map.put("amount", amount);
        map.put("id", String.valueOf(id));
        map.put("currency", currencyId.toString());
        map.put("merchant", merchantId.toString());
        try {
            tronService.processPayment(map);
            refillService.updateAddressNeedTransfer(address, merchantId, currencyId, true);
        } catch (RefillRequestAppropriateNotFoundException e) {
            log.error("request not found {}", address);
        }
    }

    private void easyTransferByPrivate(String pk, String addressTo, long amount) {
        Preconditions.checkArgument(amount > 0, "invalid amount " + amount);
        TronTransferDto tronTransferDto = new TronTransferDto(pk, addressTo, amount);
        JSONObject object = tronNodeService.transferFunds(tronTransferDto);
        boolean result = object.getJSONObject("result").getBoolean("result");
        if (!result) {
            throw new RuntimeException("error transfer to main account");
        }
    }

    private void easyTransferAssetByPrivate(String pk, String addressTo, long amount, String tokenName) {
        log.debug("transfer token {} to main account {}", tokenName, addressTo);
        Preconditions.checkArgument(amount > 0, "invalid amount " + amount);
        Preconditions.checkNotNull(tokenName);
        TronTransferDto tronTransferDto = new TronTransferDto(pk, addressTo, amount, tokenName);
        JSONObject object = tronNodeService.transferAsset(tronTransferDto);
        boolean result = object.getJSONObject("result").getBoolean("result");
        if (!result) {
            throw new RuntimeException("error transfer to main account");
        }
    }
}
