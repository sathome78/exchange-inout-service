package com.exrates.inout.service.casinocoin;

import java.math.BigDecimal;

public interface CasinoCoinTransactionService {
    BigDecimal normalizeAmountToDecimal(String amount);
}
