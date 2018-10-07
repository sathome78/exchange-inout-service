package com.exrates.inout.domain.dto;

import lombok.Data;

@Data
public class WithdrawRequestFlatAdditionalDataDto {
    private String userEmail;
    private String currencyName;
    private String merchantName;
    private String adminHolderEmail;
    private Boolean isMerchantCommissionSubtractedForWithdraw;
}
