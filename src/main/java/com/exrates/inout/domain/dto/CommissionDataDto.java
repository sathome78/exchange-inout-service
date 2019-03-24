package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.main.Commission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionDataDto {
    private BigDecimal amount;

    private BigDecimal merchantCommissionRate;
    private BigDecimal minMerchantCommissionAmount;
    private String merchantCommissionUnit;
    private BigDecimal merchantCommissionAmount;

    private Commission companyCommission;
    private BigDecimal companyCommissionRate;
    private String companyCommissionUnit;
    private BigDecimal companyCommissionAmount;

    private BigDecimal totalCommissionAmount;
    private BigDecimal resultAmount;

    private Boolean specificMerchantComissionCount;

}
