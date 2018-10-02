package com.exrates.inout.domain.other;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.main.Commission;
import com.exrates.inout.domain.main.Transaction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class WalletOperationData {

    private OperationType operationType;
    private int walletId;
    private BigDecimal amount;
    private BalanceType balanceType;
    private Commission commission;
    private BigDecimal commissionAmount;
    private TransactionSourceType sourceType;
    private Integer sourceId;
    private Transaction transaction;
    private String description;

    /**/
    public enum BalanceType {
        ACTIVE,
        RESERVED
    }

    @Override
    public String toString() {
        return "WalletOperationData{" +
                "operationType=" + operationType +
                ", walletId=" + walletId +
                ", amount=" + amount +
                ", balanceType=" + balanceType +
                ", commission=" + commission +
                ", commissionAmount=" + commissionAmount +
                ", sourceType=" + sourceType +
                ", sourceId=" + sourceId +
                ", transaction=" + transaction +
                '}';
    }
}
