package com.exrates.inout.controller.postprocessor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Log4j2
public class BeanPostProcessorForLogs implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        log.info("start init {} ", bean.getClass().getName());
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        log.info("finish init {} ", bean.getClass().getName());
        return bean;
    }
}
