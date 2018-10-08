package com.exrates.inout.service.ripple;


import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;

import java.math.BigDecimal;
import java.util.Map;

public interface RippleTransactionService {
    Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto);

    BigDecimal normalizeAmountToDecimal(String amount);

    BigDecimal getAccountBalance(String accountName);

    boolean checkSendedTransactionConsensus(String txHash, String additionalParams);
}
