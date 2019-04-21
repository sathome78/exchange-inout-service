package com.exrates.inout.service.job;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
//@Log4j2(topic = "test")
public class TestSchedulingJob {

   private static final Logger log = LogManager.getLogger("test");


    /*todo: what is it?
    @Scheduled(initialDelay = 3000, fixedDelay = 60 * 1000)
    public void foo() {
        log.debug(" ----------- Executing scheduled method at " + System.currentTimeMillis());
    }*/
}
