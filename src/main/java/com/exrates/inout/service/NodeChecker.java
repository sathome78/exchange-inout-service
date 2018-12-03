package com.exrates.inout.service;

public interface NodeChecker {

    default Long getBlocksAmount() {
        return 1L;
    }

    default boolean isAlive() {
        return getBlocksAmount() != 0;
    }
}
