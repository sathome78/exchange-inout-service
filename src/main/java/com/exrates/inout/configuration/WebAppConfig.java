package com.exrates.inout.configuration;

import com.exrates.inout.controller.interceptor.TokenInterceptor;
import com.exrates.inout.service.SSMGetter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebAppConfig implements WebMvcConfigurer {

    private final SSMGetter ssmGetter;

    @Value("{ssm.path}")
    private String inoutPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(ssmGetter.lookup(inoutPath)));
    }
}
