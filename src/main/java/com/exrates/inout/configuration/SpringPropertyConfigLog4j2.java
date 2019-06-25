package com.exrates.inout.configuration;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

public class SpringPropertyConfigLog4j2 implements ApplicationListener, Ordered {
    @Override
    public int getOrder() {
        return LoggingApplicationListener.DEFAULT_ORDER - 1;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            ConfigurableEnvironment environment = ((ApplicationEnvironmentPreparedEvent) event).getEnvironment();

            String activeProfile = environment.getProperty("spring.profiles.active");
            System.setProperty("spring.profiles.active", activeProfile != null ? activeProfile : "");

            String kibanaHostUrl = environment.getProperty("kibana.host");
            System.setProperty("kibana.host", kibanaHostUrl != null ? kibanaHostUrl : "");
        }
    }
}
