package com.exrates.inout;

import com.exrates.inout.domain.other.WalletOperationData;
import org.springframework.stereotype.Component;

@Component
public class TestRabbitReceiver {
    public void receiveMessage(WalletOperationData walletOperationData) {
        System.out.println("Received <" + walletOperationData + ">");
    }
}
