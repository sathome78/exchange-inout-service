package com.exrates.inout.service.cointest;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

//@Service
public class ActuatorTestDelete implements
        ApplicationListener<ContextRefreshedEvent> {

    private int counter = 0;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        counter++;
        if(counter == 1) new Thread(() -> {test(contextRefreshedEvent); }).start();
    }

    private void test(ContextRefreshedEvent contextRefreshedEvent) {
//        CoinTester kodTester = (CoinTester) contextRefreshedEvent.getApplicationContext().getBean("btcCoinTester");
//        try {
//            kodTester.initBot("KOD", new StringBuilder(), null);
//            kodTester.testCoin("0.017");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


}
