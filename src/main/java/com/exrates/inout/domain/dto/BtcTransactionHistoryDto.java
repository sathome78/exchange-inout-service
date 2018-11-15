package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BtcTransactionHistoryDto {
  private String txId;
  private String address;
  private String category;
  private String amount;
  private String blockhash;
  private String fee;
  private Integer confirmations;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime time;

  public BtcTransactionHistoryDto(String txId, String address, String category, String amount, Integer confirmations, LocalDateTime time) {
    this.txId = txId;
    this.address = address;
    this.category = category;
    this.amount = amount;
    this.confirmations = confirmations;
    this.time = time;
  }

  public BtcTransactionHistoryDto(String txId) {
    this.txId = txId;
  }
}
