package com.exrates.inout.service.jobs.invoice;

import com.exrates.inout.service.btc.BitcoinService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2(topic = "job")
public class btcInvoiceRequestJob {

    @Value("${btcInvoice.invoiceTimeOutIntervalMinutes}")
    private Integer EXPIRE_CLEAN_INTERVAL_MINUTES;

    @Autowired
    @Qualifier("bitcoinServiceImpl")
    BitcoinService bitcoinService;

    @Scheduled(initialDelay = 180000, fixedDelay = 1000 * 60 * 6)
    private void invoiceExpiredClean() {
        try {
            if (EXPIRE_CLEAN_INTERVAL_MINUTES > 0) {
// TODO REFILL
// Integer expireCount = bitcoinService.clearExpiredInvoices(EXPIRE_CLEAN_INTERVAL_MINUTES);
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }
}



