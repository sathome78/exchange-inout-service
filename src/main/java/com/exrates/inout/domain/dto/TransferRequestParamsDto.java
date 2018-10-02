package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestParamsDto {
    private OperationType operationType;
    private Integer merchant;
    private Integer currency;
    private BigDecimal sum;
    private String recipient;
}
