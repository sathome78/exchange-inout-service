package com.exrates.inout.event;

import org.springframework.context.ApplicationEvent;

public class BtcCoreEvent extends ApplicationEvent {
    public BtcCoreEvent(Object source) {
        super(source);
    }
}
