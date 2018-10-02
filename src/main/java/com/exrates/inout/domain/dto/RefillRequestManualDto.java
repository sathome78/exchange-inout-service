package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class RefillRequestManualDto {

    @NotNull
    private String email;
    @NotNull
    private int currency;
    private String txHash;
    @NotNull
    private String address;
    @NotNull
    private BigDecimal amount;
    private OperationType operationType = OperationType.INPUT;
    private Integer merchantId;
}
