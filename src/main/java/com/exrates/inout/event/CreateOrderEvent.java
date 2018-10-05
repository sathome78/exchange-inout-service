package com.exrates.inout.event;

import com.exrates.inout.domain.main.ExOrder;

public class CreateOrderEvent extends OrderEvent {

    public CreateOrderEvent(ExOrder source) {
        super(source);
    }
}
