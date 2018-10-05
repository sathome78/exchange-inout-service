package com.exrates.inout.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class TxReceivedByAddressFlatDto {
  private String account;
  private String address;
  private BigDecimal amount;
  private Integer confirmations;
  private String txId;
}
