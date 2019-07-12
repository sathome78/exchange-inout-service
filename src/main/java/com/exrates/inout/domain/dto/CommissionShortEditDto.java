package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
public class CommissionShortEditDto {
    private OperationType operationType;
    private String operationTypeLocalized;
    private BigDecimal value;

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getOperationTypeLocalized() {
        return operationTypeLocalized;
    }

    public void setOperationTypeLocalized(String operationTypeLocalized) {
        this.operationTypeLocalized = operationTypeLocalized;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}

