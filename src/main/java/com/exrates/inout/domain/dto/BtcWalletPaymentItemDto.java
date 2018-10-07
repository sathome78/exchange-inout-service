package com.exrates.inout.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BtcWalletPaymentItemDto {
    private String address;
    private BigDecimal amount;
}
