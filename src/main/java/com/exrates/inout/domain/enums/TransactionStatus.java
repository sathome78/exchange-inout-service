package com.exrates.inout.domain.enums;

import com.exrates.inout.exceptions.UnsupportedTransactionStatusException;

public enum TransactionStatus {

    CREATED(1),
    DELETED(2);

    private final int transactionStatus;

    TransactionStatus(int transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public static TransactionStatus convert(int id) {
        switch (id) {
            case 1:
                return CREATED;
            case 2:
                return DELETED;
            default:
                throw new UnsupportedTransactionStatusException(id);
        }
    }

    public int getStatus() {
        return transactionStatus;
    }
}
