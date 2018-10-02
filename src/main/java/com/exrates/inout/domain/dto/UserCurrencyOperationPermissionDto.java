package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.invoice.InvoiceOperationDirection;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import lombok.Data;

@Data
public class UserCurrencyOperationPermissionDto {
    private Integer userId;
    private Integer currencyId;
    private String currencyName;
    private InvoiceOperationDirection invoiceOperationDirection;
    private InvoiceOperationPermission invoiceOperationPermission;
}
