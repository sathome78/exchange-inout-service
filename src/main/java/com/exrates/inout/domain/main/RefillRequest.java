package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RefillRequest implements Serializable {
    private int id;
    private String address;
    private Integer userId;
    private String payerBankName;
    private String payerBankCode;
    private String payerAccount;
    private String userFullName;
    private String remark;
    private String receiptScan;
    private String receiptScanName;
    private BigDecimal amount;
    private Integer commissionId;
    private RefillStatusEnum status;
    private LocalDateTime dateCreation;
    private LocalDateTime statusModificationDate;
    private Integer currencyId;
    private Integer merchantId;
    private String merchantTransactionId;
    private String recipientBankName;
    private Integer recipientBankId;
    private String recipientBankAccount;
    private String recipientBankRecipient;
    private Integer adminHolderId;
    private Integer confirmations;
}
