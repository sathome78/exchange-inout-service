package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.invoice.WithdrawStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WithdrawRequestFlatForReportDto {
  private int invoiceId;
  private String wallet;
  private String recipientBank;
  private String userFullName;
  private WithdrawStatusEnum status;
  private LocalDateTime acceptanceTime;

  private String userNickname;
  private String userEmail;
  private String adminEmail;

  private BigDecimal amount;
  private BigDecimal commissionAmount;
  private LocalDateTime datetime;
  private String merchant;

  private String currency;
  private TransactionSourceType sourceType;
}
