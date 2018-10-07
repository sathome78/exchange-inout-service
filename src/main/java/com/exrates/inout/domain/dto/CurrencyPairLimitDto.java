package com.exrates.inout.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyPairLimitDto {
  private Integer currencyPairId;
  private String currencyPairName;
  private BigDecimal minRate;
  private BigDecimal maxRate;
  private BigDecimal minAmount;
  private BigDecimal maxAmount;
}
