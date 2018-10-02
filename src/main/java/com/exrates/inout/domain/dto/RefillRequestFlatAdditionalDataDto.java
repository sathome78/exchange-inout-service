package com.exrates.inout.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RefillRequestFlatAdditionalDataDto {
    private String userEmail;
    private String currencyName;
    private String merchantName;
    private String adminHolderEmail;
    private BigDecimal transactionAmount;
    private BigDecimal commissionAmount;
    private BigDecimal byBchAmount;
    private Integer confirmations;
}
