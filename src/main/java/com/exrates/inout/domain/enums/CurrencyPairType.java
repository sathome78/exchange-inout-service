package com.exrates.inout.domain.enums;


import static com.exrates.inout.domain.enums.OrderBaseType.LIMIT;

public enum CurrencyPairType {

    MAIN(LIMIT), ICO(OrderBaseType.ICO), ALL(null);

    private OrderBaseType orderBaseType;

    public OrderBaseType getOrderBaseType() {
        return orderBaseType;
    }

    CurrencyPairType(OrderBaseType orderBaseType) {
        this.orderBaseType = orderBaseType;
    }
}
