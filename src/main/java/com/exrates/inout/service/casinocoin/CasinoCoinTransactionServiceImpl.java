package com.exrates.inout.service.casinocoin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

//@Log4j2(topic = "casinocoin_log")
@Service
public class CasinoCoinTransactionServiceImpl implements CasinoCoinTransactionService {

    private static final Logger log = LogManager.getLogger("casinocoin_log");

    private final static Integer CSC_AMOUNT_MULTIPLIER = 100000000;
    private final static Integer CSC_DECIMALS = 8;

    @Override
    public BigDecimal normalizeAmountToDecimal(String amount) {
        return new BigDecimal(amount).divide(new BigDecimal(CSC_AMOUNT_MULTIPLIER)).setScale(CSC_DECIMALS, RoundingMode.HALF_DOWN);
    }

}
