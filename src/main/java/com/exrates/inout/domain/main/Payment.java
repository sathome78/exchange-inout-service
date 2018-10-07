package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.OperationType;
import lombok.Data;

@Data
public class Payment {
    private int currency;
    private int merchant;
    private double sum;
    private String destination;
    private String destinationTag;
    private String recipient;
    private OperationType operationType;

    public Payment(OperationType operationType) {
        this.operationType = operationType;
    }
}