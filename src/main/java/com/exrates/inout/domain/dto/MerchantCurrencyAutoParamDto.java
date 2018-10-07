package com.exrates.inout.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MerchantCurrencyAutoParamDto {
    private Boolean withdrawAutoEnabled;
    private Integer withdrawAutoDelaySeconds;
    private BigDecimal withdrawAutoThresholdAmount;
}
