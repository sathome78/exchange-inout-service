package com.exrates.inout.event;

import com.exrates.inout.domain.dto.BtcBlockDto;

public class BtcBlockEvent extends BtcCoreEvent {
    public BtcBlockEvent(BtcBlockDto source) {
        super(source);
    }
}
