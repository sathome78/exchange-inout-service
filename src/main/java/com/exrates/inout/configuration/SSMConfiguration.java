package com.exrates.inout.configuration;

import com.exrates.inout.properties.SsmProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.exrates.SSMGetter;
import me.exrates.SSMGetterImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2(topic = "aunit")
@RequiredArgsConstructor
public class SSMConfiguration {

    private final SsmProperties ssmProperties;

    @Bean
    public SSMGetter ssmGetter() {
        System.out.println("SSMGetter posted smth into log file");
        log.info("ебать я даун");
        if(ssmProperties.getMode().equals("dev")) return new MockSSM();

        return new SSMGetterImpl();
    }

    private class MockSSM implements SSMGetter {
        MockSSM(){
            log.info("Using mock ssm lookup...");
        }

        @Override
        public String lookup(String s) {
            return "FBSOcGiKC73j2FM";
        }
    }

}