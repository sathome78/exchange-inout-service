package com.exrates.inout.service.aisi;

import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@Log4j2(topic = "aisi_log")
public class AisiRecieveService {

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private AisiCurrencyService aisiCurrencyService;

    @Autowired
    private AisiService aisiService;

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private RefillService refillService;

    private Merchant merchant;
    private Currency currency;

    @PostConstruct
    public void init() {
        currency = currencyService.findByName(AisiServiceImpl.MERCHANT_NAME);
        merchant = merchantService.findByName(AisiServiceImpl.MERCHANT_NAME);
    }

    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 1000 * 60 * 5)
    void checkIncomePayment() {
        log.info("*** Aisi *** Scheduler start");
        List<String> listOfAddress = refillService.getListOfValidAddressByMerchantIdAndCurrency(merchant.getId(), currency.getId());

    try {
        aisiCurrencyService.getAccountTransactions().stream()
                .forEach(transaction ->
            aisiService.checkAddressForTransactionReceive(listOfAddress, transaction));
    } catch (Exception e){
        e.getStackTrace();
        log.error(e.getMessage());
    }
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }


}
