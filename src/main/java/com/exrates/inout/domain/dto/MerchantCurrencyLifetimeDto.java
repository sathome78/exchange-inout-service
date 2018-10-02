package com.exrates.inout.domain.dto;

import lombok.Data;

@Data
public class MerchantCurrencyLifetimeDto {
    private Integer merchantId;
    private Integer currencyId;
    private Integer refillLifetimeHours;
    private Integer withdrawLifetimeHours;
}
