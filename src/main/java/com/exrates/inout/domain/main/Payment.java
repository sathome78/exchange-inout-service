package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.OperationType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Payment {
    private int currency;
    private int merchant;
    private double sum;
    private String destination;
    private String destinationTag;
    private String recipient;
    private OperationType operationType;
    private User userRecipient;

    public Payment(OperationType operationType) {
        this.operationType = operationType;
    }
}