package com.exrates.inout.domain.enums;


import static com.exrates.inout.domain.enums.OrderBaseType.LIMIT;

public enum CurrencyPairType {

    MAIN(LIMIT), ICO(OrderBaseType.ICO), ALL(null);

    private OrderBaseType orderBaseType;

    CurrencyPairType(OrderBaseType orderBaseType) {
        this.orderBaseType = orderBaseType;
    }
}
