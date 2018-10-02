package com.exrates.inout.event;


import com.exrates.inout.domain.dto.BtcTransactionDto;

public class BtcWalletEvent extends BtcCoreEvent {
    public BtcWalletEvent(BtcTransactionDto source) {
        super(source);
    }
}
