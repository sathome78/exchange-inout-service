package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StopOrderSummaryDto {

    private int orderId;
    private BigDecimal stopRate;
    private OperationType operationType;

    public StopOrderSummaryDto(int orderId, BigDecimal stopRate) {
        this.orderId = orderId;
        this.stopRate = stopRate;
    }

    public StopOrderSummaryDto(int orderId, BigDecimal stopRate, OperationType operationType) {
        this.orderId = orderId;
        this.stopRate = stopRate;
        this.operationType = operationType;
    }
}
