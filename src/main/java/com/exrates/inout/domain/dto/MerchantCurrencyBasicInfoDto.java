package com.exrates.inout.domain.dto;

import lombok.Data;

@Data
public class MerchantCurrencyBasicInfoDto {
    private Integer merchantId;
    private String merchantName;
    private String currencyName;
    private Integer currencyId;
    private Integer refillScale;
    private Integer withdrawScale;
    private Integer transferScale;
}
