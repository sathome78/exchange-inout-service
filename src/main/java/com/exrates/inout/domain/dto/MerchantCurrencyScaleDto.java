package com.exrates.inout.domain.dto;


import lombok.Data;

@Data
public class MerchantCurrencyScaleDto {
    private Integer merchantId;
    private Integer currencyId;
    private Integer scaleForRefill;
    private Integer scaleForWithdraw;
    private Integer scaleForTransfer;
}
