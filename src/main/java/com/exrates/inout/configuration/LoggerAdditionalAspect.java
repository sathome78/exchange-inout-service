package com.exrates.inout.configuration;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class LoggerAdditionalAspect {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Pointcut("within(com.exrates.inout..*))")
    public void loggableMethods() {
    }

    @Before("loggableMethods()")
    public void beforeDoSomething() {
        ThreadContext.put("active.profile", activeProfile);
    }
}

