package com.exrates.inout.domain.enums;

public enum TronTransactionTypeEnum {

    TransferContract(1), TransferAssetContract(2), TriggerSmartContract(3);

    private int contractType;

    TronTransactionTypeEnum(int contractType) {
        this.contractType = contractType;
    }

    public int getContractType() {
        return contractType;
    }
}
