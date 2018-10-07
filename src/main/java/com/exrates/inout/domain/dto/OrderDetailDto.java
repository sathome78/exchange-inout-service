package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailDto {
    private int orderId;
    private OrderStatus orderStatus;
    private BigDecimal orderCreatorReservedAmount;
    private int orderCreatorReservedWalletId;
    private int transactionId;
    private OperationType transactionType;
    private BigDecimal transactionAmount;
    private int userWalletId;
    private int companyWalletId;
    private BigDecimal companyCommission;

    public OrderDetailDto(int orderId, int orderStatusId, BigDecimal orderCreatorReservedAmount, int orderCreatorReservedWalletId, int transactionId, int transactionTypeId, BigDecimal transactionAmount, int userWalletId, int companyWalletId, BigDecimal companyCommission) {
        this.orderId = orderId;
        this.orderStatus = OrderStatus.convert(orderStatusId);
        this.orderCreatorReservedAmount = orderCreatorReservedAmount;
        this.orderCreatorReservedWalletId = orderCreatorReservedWalletId;
        this.transactionId = transactionId;
        if (transactionTypeId != 0) {
            this.transactionType = OperationType.convert(transactionTypeId);
        }
        this.transactionAmount = transactionAmount;
        this.userWalletId = userWalletId;
        this.companyWalletId = companyWalletId;
        this.companyCommission = companyCommission;
    }
}

