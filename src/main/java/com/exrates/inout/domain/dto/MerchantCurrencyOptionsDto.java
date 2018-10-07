package com.exrates.inout.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MerchantCurrencyOptionsDto {
    private Integer merchantId;
    private Integer currencyId;
    private String merchantName;
    private String currencyName;
    private BigDecimal inputCommission;
    private BigDecimal outputCommission;
    private BigDecimal transferCommission;
    private BigDecimal minFixedCommission;
    private Boolean isRefillBlocked;
    private Boolean isWithdrawBlocked;
    private Boolean isTransferBlocked;
    private Boolean withdrawAutoEnabled;
    private Integer withdrawAutoDelaySeconds;
    private BigDecimal withdrawAutoThresholdAmount;
    private Boolean isMerchantCommissionSubtractedForWithdraw;

}
