package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.ActionType;
import com.exrates.inout.util.BigDecimalProcessing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BtcAdminPreparedTxDto {
    private static final BigDecimal BTC_KB_TO_SAT_BYTE_MULTIPLIER = BigDecimal.valueOf(100_000);

    @JsonIgnore
    private final List<BtcPreparedTransactionDto> preparedTransactions;

    private final List<BtcTxPaymentDto> payments;
    private final BigDecimal feeRate;
    private final BigDecimal feeRateSatoshiByte;
    private final BigDecimal totalFeeAmount;

    public BtcAdminPreparedTxDto(List<BtcPreparedTransactionDto> preparedTransactions, BigDecimal feeRate) {
        this.preparedTransactions = preparedTransactions;
        this.payments = preparedTransactions.stream().flatMap(tx -> tx.getPayments().entrySet().stream())
                .map(entry -> new BtcTxPaymentDto(entry.getKey(), null, entry.getValue(), null))
                .collect(Collectors.toList());
        this.feeRate = feeRate;
        this.feeRateSatoshiByte = BigDecimalProcessing.doAction(feeRate, BTC_KB_TO_SAT_BYTE_MULTIPLIER, ActionType.MULTIPLY);
        this.totalFeeAmount = preparedTransactions.stream().map(BtcPreparedTransactionDto::getFeeAmount)
                .reduce((acc, elem ) -> BigDecimalProcessing.doAction(acc, elem, ActionType.ADD)).orElse(BigDecimal.valueOf(-1));
    }


}
