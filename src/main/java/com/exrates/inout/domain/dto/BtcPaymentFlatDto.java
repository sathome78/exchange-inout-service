package com.exrates.inout.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BtcPaymentFlatDto {
  private BigDecimal amount;
  private Integer confirmations;
  private String txId;
  private String address;
  private String blockhash;
  private Integer merchantId;
  private Integer currencyId;
}
