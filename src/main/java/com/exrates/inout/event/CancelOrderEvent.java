package com.exrates.inout.event;

import com.exrates.inout.domain.main.ExOrder;

public class CancelOrderEvent extends OrderEvent {

    private boolean byAdmin;

    public boolean isByAdmin() {
        return byAdmin;
    }

    public CancelOrderEvent(ExOrder source, boolean byAdmin) {
        super(source);
        this.byAdmin = byAdmin;
    }
}
