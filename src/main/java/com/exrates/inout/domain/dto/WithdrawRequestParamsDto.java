package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawRequestParamsDto {
    private Integer currency;
    private Integer merchant;
    private BigDecimal sum;
    private String destination;
    private String destinationTag;
    private int merchantImage;
    private OperationType operationType;
    private String recipientBankName;
    private String recipientBankCode;
    private String userFullName;
    private String remark;
    private String walletNumber;
}
