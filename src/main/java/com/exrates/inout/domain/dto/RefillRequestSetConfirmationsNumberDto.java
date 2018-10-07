package com.exrates.inout.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class RefillRequestSetConfirmationsNumberDto {
    private Integer requestId;
    private String address;
    private Integer merchantId;
    private Integer currencyId;
    private Integer confirmations;
    private String hash;
    private BigDecimal amount;
    private String blockhash;
}
