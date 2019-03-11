package com.exrates.inout.configuration;

import com.exrates.inout.properties.SsmProperties;
import com.exrates.inout.service.SSMGetter;
import com.exrates.inout.service.SSMGetterImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class SSMConfiguration {

    private final SsmProperties ssmProperties;

    @Bean
    public SSMGetter ssmGetter() {
        if(ssmProperties.getMode().equals("dev")) return new MockSSM();

        return new SSMGetterImpl();
    }

    private class MockSSM implements SSMGetter {
        MockSSM(){
            log.info("Using mock ssm lookup...");
        }

        @Override
        public String lookup(String s) {
            return "MOCK_TOKEN";
        }
    }
}