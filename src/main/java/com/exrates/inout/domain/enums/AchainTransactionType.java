package com.exrates.inout.domain.enums;

import java.util.Arrays;

public enum AchainTransactionType {

    CONTRACT_CALL("call_contract_op_type"),
    SIMPLE_TRANSFER("deposit_op_type");


    private String value;

    public String getValue() {
        return value;
    }

    AchainTransactionType(String value) {
        this.value = value;
    }

    public static AchainTransactionType convert(String name) {
        return Arrays.stream(AchainTransactionType.class.getEnumConstants())
                .filter(e -> e.getValue().equals(name))
                .findAny()
                .orElseThrow(() -> new RuntimeException("unsupported type " + name));
    }
}
