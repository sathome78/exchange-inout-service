package com.exrates.inout.service.casinocoin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

//@Log4j2(topic = "casinocoin_log")
@Service
@PropertySource("classpath:/merchants/casinocoin.properties")
public class CasinoCoinTransactionServiceImpl implements CasinoCoinTransactionService {

   private static final Logger log = LogManager.getLogger("casinocoin_log");


    @Value("${casinocoin.amount.multiplier}")
    private Integer cscAmountMultiplier;

    @Value("${casinocoin.decimals}")
    private Integer cscDecimals;

    @Override
    public BigDecimal normalizeAmountToDecimal(String amount) {
        return new BigDecimal(amount).divide(new BigDecimal(cscAmountMultiplier)).setScale(cscDecimals, RoundingMode.HALF_DOWN);
    }

}
