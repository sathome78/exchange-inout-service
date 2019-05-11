package com.exrates.inout.service;

public interface CoinTester {
    void initBot(String name, StringBuilder stringBuilder, String email) throws Exception;
    void testCoin(String refillAmount) throws Exception;
}
