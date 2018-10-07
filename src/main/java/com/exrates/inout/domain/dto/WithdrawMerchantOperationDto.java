package com.exrates.inout.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WithdrawMerchantOperationDto {
  private String currency;
  private String amount;
  private String accountTo;
  private String destinationTag;
}
