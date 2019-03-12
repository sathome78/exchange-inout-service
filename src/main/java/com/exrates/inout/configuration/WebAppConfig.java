package com.exrates.inout.configuration;

import com.exrates.inout.controller.interceptor.TokenInterceptor;
import com.exrates.inout.properties.SsmProperties;
import lombok.RequiredArgsConstructor;
import me.exrates.SSMGetter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebAppConfig implements WebMvcConfigurer {

    private final SSMGetter ssmGetter;

    private final SsmProperties ssmProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(ssmGetter.lookup(ssmProperties.getPath())));
    }
}
