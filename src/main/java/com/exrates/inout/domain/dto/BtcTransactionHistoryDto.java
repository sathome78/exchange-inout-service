package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
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
}
