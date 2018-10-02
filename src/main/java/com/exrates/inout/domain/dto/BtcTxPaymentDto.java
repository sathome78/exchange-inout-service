package com.exrates.inout.domain.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BtcTxPaymentDto {

    private String address;
    private String category;
    private BigDecimal amount;
    private BigDecimal fee;
}
