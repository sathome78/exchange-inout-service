package com.exrates.inout.service.config;

import com.exrates.inout.configuration.DBConfiguration;
import com.exrates.inout.configuration.SSMConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"com.exrates.inout.service.impl", "com.exrates.inout.dao", "com.exrates.inout.util.ssm"})
@Import({DBConfiguration.class, SSMConfiguration.class})
public class TestRefillServiceConfig {

}
