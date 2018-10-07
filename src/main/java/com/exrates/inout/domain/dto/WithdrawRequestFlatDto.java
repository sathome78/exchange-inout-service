package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.enums.invoice.WithdrawStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WithdrawRequestFlatDto {
    private int id;
    private String wallet;
    private String destinationTag;
    private Integer userId;
    private String recipientBankName;
    private String recipientBankCode;
    private String userFullName;
    private String remark;
    private BigDecimal amount;
    private BigDecimal commissionAmount;
    private BigDecimal merchantCommissionAmount;
    private Integer commissionId;
    private WithdrawStatusEnum status;
    private LocalDateTime dateCreation;
    private LocalDateTime statusModificationDate;
    private Integer currencyId;
    private Integer merchantId;
    private Integer adminHolderId;
    private InvoiceOperationPermission invoiceOperationPermission;
    private String transactionHash;
    private String additionalParams;
}
