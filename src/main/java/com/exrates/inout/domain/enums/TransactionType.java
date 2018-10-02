package com.exrates.inout.domain.enums;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static com.exrates.inout.domain.enums.TransactionSourceType.*;

public enum TransactionType {
    REFILL_IN(REFILL, null),
    WITHDRAW_OUT(WITHDRAW, OperationType.OUTPUT),
    ORDER_IN(ORDER, OperationType.INPUT),
    ORDER_OUT(ORDER, OperationType.OUTPUT),
    RESERVE_TO(null, OperationType.WALLET_INNER_TRANSFER, (v) -> v.compareTo(BigDecimal.ZERO) < 0),
    RESERVE_FROM(null, OperationType.WALLET_INNER_TRANSFER, (v) -> v.compareTo(BigDecimal.ZERO) >= 0),
    REFERRAL(TransactionSourceType.REFERRAL, null),
    MANUAL(TransactionSourceType.MANUAL, null),
    USER_TRANSFER_IN(USER_TRANSFER, OperationType.INPUT),
    USER_TRANSFER_OUT(USER_TRANSFER, OperationType.OUTPUT),
    NOTIFICATIONS(TransactionSourceType.NOTIFICATIONS, null);

    private TransactionSourceType sourceType;
    private OperationType operationType;
    private Predicate<BigDecimal> amountPredicate = null;

    TransactionType(TransactionSourceType sourceType, OperationType operationType) {
        this.sourceType = sourceType;
        this.operationType = operationType;
    }

    TransactionType(TransactionSourceType sourceType, OperationType operationType, Predicate<BigDecimal> amountPredicate) {
        this.sourceType = sourceType;
        this.operationType = operationType;
        this.amountPredicate = amountPredicate;
    }

    public TransactionSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(TransactionSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Predicate<BigDecimal> getAmountPredicate() {
        return amountPredicate;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String toString() {
        return this.name();
    }
}
