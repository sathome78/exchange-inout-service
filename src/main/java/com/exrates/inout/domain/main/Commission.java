package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.UserRole;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Commission {
    private int id;
    private OperationType operationType;
    private BigDecimal value;
    private Date dateOfChange;
    private UserRole userRole;

    public Commission() {
    }

    public Commission(int id) {
        this.id = id;
    }

    public static Commission zeroComission() {
        Commission commission = new Commission();
        commission.setId(24);
        commission.setOperationType(OperationType.OUTPUT);
        commission.setValue(BigDecimal.ZERO);
        commission.setDateOfChange(new Date());
        return commission;
    }
}