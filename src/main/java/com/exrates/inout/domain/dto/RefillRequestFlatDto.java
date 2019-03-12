package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RefillRequestFlatDto extends RequestWithRemarkAbstractDto {
    private int id;
    private String address;
    private String privKey;
    private String pubKey;
    private String brainPrivKey;
    private Integer userId;
    private String payerAccount;
    private String userFullName;
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
    private String merchantRequestSign;
    private Integer adminHolderId;
    private Integer refillRequestAddressId;
    private Integer refillRequestParamId;
    private InvoiceOperationPermission invoiceOperationPermission;

}
