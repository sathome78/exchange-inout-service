package com.exrates.inout.service.job.invoice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.service.BitcoinService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by ValkSam
 */
@Service
@PropertySource(value = {"classpath:/job.properties"})
//@Log4j2(topic = "job")
@EnableAutoConfiguration
public class btcInvoiceRequestJob {

   private static final Logger log = LogManager.getLogger("job");


  @Value("${btcInvoice.invoiceTimeOutIntervalMinutes}")
  private Integer EXPIRE_CLEAN_INTERVAL_MINUTES;

  @Autowired
  @Qualifier("bitcoinServiceImpl")
  BitcoinService bitcoinService;

  @Scheduled(initialDelay = 180000, fixedDelay = 1000 * 60 * 6)
  private void invoiceExpiredClean() throws Exception {
    try {
      if (EXPIRE_CLEAN_INTERVAL_MINUTES > 0) {
// TODO REFILL
// Integer expireCount = bitcoinService.clearExpiredInvoices(EXPIRE_CLEAN_INTERVAL_MINUTES);
      }
    } catch (Exception e){
      //log.error(ExceptionUtils.getStackTrace(e));
    }
  }

}



