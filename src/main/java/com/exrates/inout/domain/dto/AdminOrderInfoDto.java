package com.exrates.inout.domain.dto;

import lombok.Data;

@Data
public class AdminOrderInfoDto {

    private boolean isAcceptable;
    private String notification;
    private OrderInfoDto orderInfo;

    public AdminOrderInfoDto(OrderInfoDto orderInfo) {
        this.orderInfo = orderInfo;
    }
}
