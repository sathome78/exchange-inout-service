package com.exrates.inout.domain;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.OrderStatus;
import com.exrates.inout.domain.main.CurrencyPair;
import com.exrates.inout.domain.main.ExOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class StopOrder {

    private int id;
    private int userId;
    private BigDecimal stop;
    private BigDecimal limit;
    private BigDecimal amountBase;
    private BigDecimal amountConvert;
    private int currencyPairId;
    private OperationType operationType;
    private Integer childOrderId;
    private OrderStatus status;
    private LocalDateTime dateCreation;
    private LocalDateTime modificationDate;
    private int comissionId;
    private BigDecimal commissionFixedAmount;
    private CurrencyPair currencyPair;

    public StopOrder() {
    }

    public StopOrder(ExOrder exOrder) {
        this.id = exOrder.getId();
        this.userId = exOrder.getUserId();
        this.currencyPairId = exOrder.getCurrencyPair().getId();
        this.operationType = exOrder.getOperationType();
        this.stop = exOrder.getStop();
        this.limit = exOrder.getExRate();
        this.amountBase = exOrder.getAmountBase();
        this.amountConvert = exOrder.getAmountConvert();
        this.comissionId = exOrder.getComissionId();
        this.commissionFixedAmount = exOrder.getCommissionFixedAmount();
        this.status = exOrder.getStatus();
        this.currencyPair = exOrder.getCurrencyPair();
    }

}
