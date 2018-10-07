package com.exrates.inout.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class InvoiceConfirmData extends RequestWithRemarkAbstractDto {
    @NotNull
    private Integer invoiceId;
    @NotNull
    private String payerBankName;
    private String payerBankCode;

    @NotNull
    private String userAccount;
    @NotNull
    private String userFullName;
    @NotNull
    private MultipartFile receiptScan;
    private String receiptScanName;
    private String receiptScanPath;


}
