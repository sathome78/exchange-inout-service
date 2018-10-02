package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
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
