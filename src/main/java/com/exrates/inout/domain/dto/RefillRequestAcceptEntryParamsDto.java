package com.exrates.inout.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class RefillRequestAcceptEntryParamsDto {
    private Integer requestId;
    private BigDecimal amount;
    private String remark;
    private String merchantTxId;
}
