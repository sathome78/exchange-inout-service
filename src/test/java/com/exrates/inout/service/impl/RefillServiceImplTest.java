package com.exrates.inout.service.impl;

import com.exrates.inout.InoutApplication;
import com.exrates.inout.service.btc.BitcoinServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = InoutApplication.class)
@RunWith(SpringRunner.class)
public class RefillServiceImplTest {

    @Autowired
    private RefillServiceImpl refillService;

    @MockBean(name = "bitcoinServiceImpl")
    private BitcoinServiceImpl bitcoinService;

    @Test
    public void createRefillRequest() {
        System.out.println("DONWED");
    }
}