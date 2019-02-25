package com.exrates.inout.configuration;


import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!dev")
public class SSMConfiguration {

    @Bean
    public AWSSimpleSystemsManagement awsSimpleSystemsManagement() {
        return AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }
}