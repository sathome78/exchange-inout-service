package com.exrates.inout.configuration;

import com.exrates.inout.configuration.ext.LogableErrorHandler;
import com.exrates.inout.controller.postprocessor.BeanPostProcessorForLogs;
import com.exrates.inout.exceptions.handlers.RestResponseErrorHandler;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.QiwiProperty;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.zeromq.ZMQ;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.Locale;

@Configuration
public class OtherConfiguration {

    private final CryptoCurrencyProperties cryptoCurrencyProperties;

    public OtherConfiguration(CryptoCurrencyProperties cryptoCurrencyProperties) {
        this.cryptoCurrencyProperties = cryptoCurrencyProperties;
    }

    @Bean
    public BeanPostProcessor onlineMethodPostProcessor() {
        return new BeanPostProcessorForLogs();
    }

    @Bean
    @Primary
    public RestTemplate restTemplate(LogableErrorHandler errorHandler) {
        HttpClientBuilder b = HttpClientBuilder.create();
        HttpClient client = b.build();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestResponseErrorHandler());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(client);
        requestFactory.setConnectionRequestTimeout(5*1000);
        requestFactory.setReadTimeout(25000);
        restTemplate.setErrorHandler(errorHandler);
        restTemplate.setRequestFactory(requestFactory);
        return new RestTemplate();
    }


    @Bean("qiwiRestTemplate")
    public RestTemplate qiwiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        QiwiProperty qiwiProperty = cryptoCurrencyProperties.getPaymentSystemMerchants().getQiwi();
        restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(qiwiProperty.getClientId(), qiwiProperty.getClientSecret()));
        return restTemplate;
    }

    @Bean
    public Client client() {
        Client build = ClientBuilder.newBuilder().build();
        build.register(new LoggingFilter());
        return build;
    }

    @Bean
    public ZMQ.Context zmqContext() {
        return ZMQ.context(1);
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("en"));
        resolver.setCookieName("myAppLocaleCookie");
        resolver.setCookieMaxAge(3600);
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
}

