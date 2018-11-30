package com.exrates.inout.service;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;

public interface NodeChecker {

    Long getBlocksAmount() throws BitcoindException, CommunicationException;

    Long getBlocksAmountFromExplorer();

    default boolean isSynchronized() throws BitcoindException, CommunicationException {
        long module = Math.abs(getBlocksAmount() / getBlocksAmountFromExplorer());
        return module > 0.95 && module < 1.05;
    }

    default boolean isAlive() throws BitcoindException, CommunicationException {
        return getBlocksAmount() != 0;
    }
}
