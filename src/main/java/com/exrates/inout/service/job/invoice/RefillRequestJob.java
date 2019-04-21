package com.exrates.inout.service.job.invoice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.domain.dto.BtcTransactionHistoryDto;
import com.exrates.inout.service.BitcoinService;
import com.exrates.inout.service.IMerchantService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.impl.MerchantServiceContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//exrates.model.dto.BtcTransactionHistoryDto;
//exrates.service.BitcoinService;
//exrates.service.MerchantService;
//exrates.service.RefillService;
//exrates.service.merchantStrategy.IMerchantService;
//exrates.service.merchantStrategy.MerchantServiceContext;

/**
 * Created by ValkSam
 */
@Service
//@Log4j2
public class RefillRequestJob {

   private static final Logger log = LogManager.getLogger(RefillRequestJob.class);


    @Autowired
    RefillService refillService;

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MerchantServiceContext serviceContext;

    @Scheduled(initialDelay = 180000, fixedDelay = 1000 * 60 * 5)
    private void refillExpiredClean() throws Exception {
        log.debug("\nstart expired refill cleaning ... ");
        Integer expireCount = refillService.clearExpiredInvoices();
        log.debug("\n... end expired refill cleaning. Mark as expired: " + expireCount);
    }

    /**
     * Method for check unprocessed transactions for coins in merchantNames array.
     * Because blocknotify doesn't work correctly (!!! need to check node config and node config properties !!!)
     * During the check processBtcPayment is executed, which create refill request and refill user wallet.
     */
    @Scheduled(initialDelay = 0, fixedDelay = 1000 * 60 * 5)
    public void refillCheckPaymentsForCoins() {

        log.info("Starting refillCheckPaymentsForCoins");
        String[] merchantNames = new String[]{"QRK", "LBTC", "LPC", "XFC", "DDX", "MBC", "BTCP", "CLX", "ABBC", "CBC", "BTCZ", "KOD", "RIME", "DIVI", "KOD"};
        for (String coin : merchantNames) {
            try {
                getBitcoinServiceByMerchantName(coin).scanForUnprocessedTransactions(null);
            } catch (Exception e) {
                if(coin.equals("KOD"))
                e.printStackTrace();
            }
        }

    }

    @Scheduled(initialDelay = 0, fixedDelay = 1000 * 60 * 5)
    public void refillPaymentsForNonSupportedCoins() {
            String[] merchantNames = new String[]{"Q", "DIME"};
            for (String merchantName : merchantNames) {
                try {
                    BitcoinService service = getBitcoinServiceByMerchantName(merchantName);
                    List<BtcTransactionHistoryDto> transactions = service.listAllTransactions();

                for (BtcTransactionHistoryDto transaction : transactions) {
                    if (transaction.getConfirmations() >= service.minConfirmationsRefill()) {
                        Map<String, String> params = new LinkedHashMap<>();
                        params.put("txId", transaction.getTxId());
                        params.put("address", transaction.getAddress());
                        forceRefill(merchantName, params);
                    }
                }
                } catch (Exception e) {
                    log.error(e);
                }
            }

    }

    private void forceRefill(String merchantName, Map<String, String> params) {
        try {
            getBitcoinServiceByMerchantName(merchantName).processPayment(params);
        } catch (Exception e) {
            //log.error(e);
        }
    }

    private BitcoinService getBitcoinServiceByMerchantName(String merchantName) {
        String serviceBeanName = merchantService.findByName(merchantName).getServiceBeanName();
        IMerchantService merchantService = serviceContext.getMerchantService(serviceBeanName);
        if (merchantService == null || !(merchantService instanceof BitcoinService)) {
            throw new RuntimeException(serviceBeanName);
        }
        return (BitcoinService) merchantService;
    }

}



