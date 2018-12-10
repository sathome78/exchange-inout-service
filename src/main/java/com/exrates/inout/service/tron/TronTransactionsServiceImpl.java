package com.exrates.inout.service.tron;

import com.exrates.inout.domain.dto.RefillRequestAddressDto;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.domain.dto.TronTransferDto;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.RefillService;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Log4j2(topic = "tron")
@Service
public class TronTransactionsServiceImpl implements TronTransactionsService {

    @Value("${tron.node.main-account-hex-address}")
    private String mainAddressHex;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledExecutorService transferScheduler = Executors.newScheduledThreadPool(1);

    private final TronNodeService tronNodeService;
    private final TronService tronService;
    private final RefillService refillService;

    @Autowired
    public TronTransactionsServiceImpl(TronNodeService tronNodeService,
                                       TronService tronService,
                                       RefillService refillService) {
        this.tronNodeService = tronNodeService;
        this.tronService = tronService;
        this.refillService = refillService;
    }

    @PostConstruct
    private void init() {
        scheduler.scheduleAtFixedRate(this::checkUnconfirmedJob, 3, 5, TimeUnit.MINUTES);
        transferScheduler.scheduleAtFixedRate(this::transferToMainAccountJob, 3, 20, TimeUnit.MINUTES);
    }

    private void checkUnconfirmedJob() {
        List<RefillRequestFlatDto> dtos = refillService.getInExamineWithChildTokensByMerchantIdAndCurrencyIdList(tronService.getMerchantId(), tronService.getCurrencyId());
        dtos.forEach(p -> {
            try {
                if (checkIsTransactionConfirmed(p.getMerchantTransactionId())) {
                    processTransaction(p.getAddress(), p.getMerchantTransactionId(), p.getAmount().toString());
                }
            } catch (Exception e) {
                log.error(e);
            }
        });
    }

    private void transferToMainAccountJob() {
        List<RefillRequestAddressDto> listRefillRequestAddressDto = refillService.findAllAddressesNeededToTransfer(tronService.getMerchantId(), tronService.getCurrencyId());
        listRefillRequestAddressDto.forEach(p -> {
            try {
                transferToMainAccount(p);
                refillService.updateAddressNeedTransfer(p.getAddress(), tronService.getMerchantId(), tronService.getCurrencyId(), false);
            } catch (Exception e) {
                log.error(e);
            }
        });
    }

    private void transferToMainAccount(RefillRequestAddressDto dto) {
        Long accountAmount = tronNodeService.getAccount(dto.getAddress()).getJSONObject("data").getLong("balance");
        easyTransferByPrivate(dto.getPrivKey(), mainAddressHex, accountAmount);
    }

    public boolean checkIsTransactionConfirmed(String txHash) {
        JSONObject rawResponse = tronNodeService.getTransaction(txHash);
        return rawResponse.getBoolean("confirmed");
    }

    public void processTransaction(String address, String hash, String amount) {
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        map.put("hash", hash);
        map.put("amount", amount);
        try {
            tronService.processPayment(map);
            refillService.updateAddressNeedTransfer(address, tronService.getMerchantId(), tronService.getCurrencyId(), true);
        } catch (RefillRequestAppropriateNotFoundException e) {
            log.error("request not found {}", address);
        }
    }

    private void easyTransferByPrivate(String pk, String addressTo, long amount) {
        TronTransferDto tronTransferDto = new TronTransferDto(pk, addressTo, amount);
        JSONObject object = tronNodeService.transferFunds(tronTransferDto);
        boolean result = object.getJSONObject("result").getBoolean("result");
        if (!result) {
            throw new RuntimeException("erro trnasfer to main account");
        }
    }
}
