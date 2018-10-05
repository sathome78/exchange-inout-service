package com.exrates.inout.event;

import com.exrates.inout.domain.main.ExOrder;

public class AcceptOrderEvent extends OrderEvent {
    public AcceptOrderEvent(ExOrder source) {
        super(source);
    }
}
