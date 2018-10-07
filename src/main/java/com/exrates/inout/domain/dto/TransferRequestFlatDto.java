package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.enums.invoice.TransferStatusEnum;
import com.exrates.inout.domain.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransferRequestFlatDto {
    private int id;
    private BigDecimal amount;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateCreation;
    private TransferStatusEnum status;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime statusModificationDate;
    private Integer merchantId;
    private Integer currencyId;
    private Integer userId;
    private Integer recipientId;
    private BigDecimal commissionAmount;
    private Integer commissionId;
    private String hash;
    private String initiatorEmail;
    private String merchantName;
    private String creatorEmail;
    private String recipientEmail;
    private String currencyName;
    private InvoiceOperationPermission invoiceOperationPermission;
}
