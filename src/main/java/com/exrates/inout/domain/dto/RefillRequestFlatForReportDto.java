package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class RefillRequestFlatForReportDto {
    private int id;
    private String address;
    private String recipientBankName;
    private String userFullName;
    private RefillStatusEnum status;
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
