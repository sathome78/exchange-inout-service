package com.exrates.inout.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TxReceivedByAddressFlatDto {
  private String account;
  private String address;
  private BigDecimal amount;
  private Integer confirmations;
  private String txId;
}
