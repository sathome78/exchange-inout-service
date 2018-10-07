package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefillRequestParamsDto {
    private OperationType operationType;
    private Integer currency;
    private BigDecimal sum;
    private Integer merchant;
    private Integer recipientBankId;
    private String recipientBankCode;
    private String recipientBankName;
    private String recipient;
    private String userFullName;
    private String remark;
    private String merchantRequestSign;
    private String address;
    private Boolean generateNewAddress;

    public RefillRequestParamsDto(RefillRequestManualDto refillDto) {
        this.operationType = refillDto.getOperationType();
        this.merchant = refillDto.getMerchantId();
        this.currency = refillDto.getCurrency();
        this.sum = refillDto.getAmount();
        this.address = refillDto.getAddress();
        this.generateNewAddress = false;
    }

    public RefillRequestParamsDto() {
    }
}
