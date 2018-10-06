package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class RefillRequestBtcInfoDto {
  private Integer id;
  private String address;
  private String txId;
  private BigDecimal amount;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime dateCreation;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime dateModification;
  private String userEmail;
  private String status;
  
}
