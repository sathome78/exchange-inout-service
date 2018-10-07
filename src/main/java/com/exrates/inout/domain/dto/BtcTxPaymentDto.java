package com.exrates.inout.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BtcTxPaymentDto {

    private String address;
    private String category;
    private BigDecimal amount;
    private BigDecimal fee;
}
