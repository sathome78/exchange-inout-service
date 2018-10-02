package com.exrates.inout.domain.enums;

import com.exrates.inout.exceptions.UnsupportedOperationTypeException;
import org.springframework.context.MessageSource;

import java.util.*;

import static com.exrates.inout.domain.enums.TransactionSourceType.REFILL;
import static com.exrates.inout.domain.enums.TransactionSourceType.WITHDRAW;

public enum OperationType {
    INPUT(1, REFILL) {{
        currencyForAddRandomValueToAmount.put(10, new AdditionalRandomAmountParam() {{
            currencyName = "IDR";
            lowBound = 100;
            highBound = 999;
        }});
    }},
    OUTPUT(2, WITHDRAW),
    SELL(3),
    BUY(4),
    WALLET_INNER_TRANSFER(5),
    REFERRAL(6),
    STORNO(7),
    MANUAL(8),
    USER_TRANSFER(9);

    public class AdditionalRandomAmountParam {
        public String currencyName;
        public double lowBound;
        public double highBound;

        public boolean equals(Object currencyName) {
            return this.currencyName.equals((String) currencyName);
        }

        public int hashCode() {
            return currencyName != null ? currencyName.hashCode() : 0;
        }
    }

    public final int type;

    TransactionSourceType transactionSourceType = null;

    protected final Map<Integer, AdditionalRandomAmountParam> currencyForAddRandomValueToAmount = new HashMap<>();

    OperationType(int type) {
        this.type = type;
    }

    OperationType(int type, TransactionSourceType transactionSourceType) {
        this.type = type;
        this.transactionSourceType = transactionSourceType;
    }

    public Optional<AdditionalRandomAmountParam> getRandomAmountParam(Integer currencyId) {
        return Optional.ofNullable(currencyForAddRandomValueToAmount.get(currencyId));
    }

    public int getType() {
        return type;
    }

    public TransactionSourceType getTransactionSourceType() {
        return transactionSourceType;
    }

    public static OperationType convert(int id) {
        return Arrays.stream(OperationType.class.getEnumConstants())
                .filter(e -> e.type == id)
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationTypeException(id));
    }

    public String toString(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage("operationtype." + this.name(), null, locale);
    }
}
