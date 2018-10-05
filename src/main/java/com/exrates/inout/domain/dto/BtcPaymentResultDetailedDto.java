package com.exrates.inout.domain.dto;


import com.exrates.inout.util.BigDecimalProcessing;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BtcPaymentResultDetailedDto {
    private String address;
    private String amount;
    private String txId;
    private String error;

    public BtcPaymentResultDetailedDto() {
    }

    public BtcPaymentResultDetailedDto(String address, BigDecimal amount, BtcPaymentResultDto btcPaymentResultDto) {
        this.address = address;
        this.amount = BigDecimalProcessing.formatNonePoint(amount, false);
        this.txId = btcPaymentResultDto.getTxId();
        this.error = btcPaymentResultDto.getError();
    }
}
