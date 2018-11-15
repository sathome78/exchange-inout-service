package com.exrates.inout.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TxReceivedByAddressFlatDto {
  private String account;
  private String address;
  private BigDecimal amount;
  private Integer confirmations;
  @JsonProperty(value = "txid")
  private String txId;
  private String category;
  private String abandoned;
}
