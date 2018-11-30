package com.exrates.inout.service;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;

public interface NodeChecker {

    default Long getBlocksAmount() throws BitcoindException, CommunicationException {
        return 1L;
    }

    default Long getBlocksAmountFromExplorer() {
        return 1L;
    }

    default boolean isSynchronized() throws BitcoindException, CommunicationException {
        long module = Math.abs(getBlocksAmount() / getBlocksAmountFromExplorer());
        return module > 0.95 && module < 1.05;
    }

    default boolean isAlive() throws BitcoindException, CommunicationException {
        return getBlocksAmount() != 0;
    }
}
