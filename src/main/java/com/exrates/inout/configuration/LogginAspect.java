package com.exrates.inout.configuration;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogginAspect {

    private static final Logger log = LogManager.getLogger("logginAspect");

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void beanAnnotatedWithRestTemplate() {
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {
    }

    @Pointcut("publicMethod() && beanAnnotatedWithRestTemplate()")
    public void pointCatRestController() {
    }

    @AfterReturning(value = "pointCatRestController()", returning = "retVal")
    public void logAroundGetEmployee(Object retVal) {
        log.debug("\nResponse: {} \n", retVal);
    }

}
