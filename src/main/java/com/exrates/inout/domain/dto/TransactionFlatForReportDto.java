package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionFlatForReportDto {
  private int transactionId;

  private String merchant;
  private LocalDateTime providedDate;

  private String userNickname;
  private String userEmail;

  private BigDecimal amount;
  private BigDecimal commissionAmount;
  private LocalDateTime datetime;
  private Integer confirmation;
  private Boolean provided;
  private TransactionSourceType sourceType;

  private String currency;
  private OperationType operationType;
}
