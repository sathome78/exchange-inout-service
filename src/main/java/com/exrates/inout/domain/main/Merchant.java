package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.MerchantProcessType;
import lombok.Data;

import java.io.Serializable;

@Data
public class Merchant implements Serializable {
    private int id;
    private String name;
    private String description;
    private String serviceBeanName;
    private MerchantProcessType processType;
    private Integer refillOperationCountLimitForUserPerDay;
    private Boolean additionalTagForWithdrawAddressIsUsed;

    public Merchant() {
    }

    public Merchant(int id) {
        this.id = id;
    }

    public Merchant(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}