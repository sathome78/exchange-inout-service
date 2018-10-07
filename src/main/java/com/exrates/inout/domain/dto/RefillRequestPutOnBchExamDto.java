package com.exrates.inout.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class RefillRequestPutOnBchExamDto {
  private Integer requestId;
  private Integer merchantId;
  private Integer currencyId;
  private BigDecimal amount;
  private String address;
  private String hash;
  private String blockhash;
}
