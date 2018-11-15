package com.exrates.inout.domain.dto;


import com.exrates.inout.util.BigDecimalProcessing;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BtcPaymentResultDetailedDto {
    private String address;
    private String amount;
    private String txId;
    private String error;


    public BtcPaymentResultDetailedDto(String address, BigDecimal amount, BtcPaymentResultDto btcPaymentResultDto) {
        this.address = address;
        this.amount = BigDecimalProcessing.formatNonePoint(amount, false);
        this.txId = btcPaymentResultDto.getTxId();
        this.error = btcPaymentResultDto.getError();
    }

    public BtcPaymentResultDetailedDto(String address, String amount, String txId, String error) {
        this.address = address;
        this.amount = amount;
        this.txId = txId;
        this.error = error;
    }
}
