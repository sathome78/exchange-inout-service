package com.exrates.inout.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@ToString
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
