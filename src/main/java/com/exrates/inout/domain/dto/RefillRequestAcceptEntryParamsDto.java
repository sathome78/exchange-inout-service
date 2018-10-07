package com.exrates.inout.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefillRequestAcceptEntryParamsDto {
    private Integer requestId;
    private BigDecimal amount;
    private String remark;
    private String merchantTxId;
}
