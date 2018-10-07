package com.exrates.inout.domain.dto;

import lombok.Data;

@Data
public class OrdersListWrapper {

    private Object data;
    private String type;
    private int currencyPairId;


    public OrdersListWrapper(Object data, String type, int currencyPairId) {
        this.data = data;
        this.type = type;
        this.currencyPairId = currencyPairId;
    }

    public OrdersListWrapper(Object data, String type) {
        this.data = data;
        this.type = type;
    }
}
