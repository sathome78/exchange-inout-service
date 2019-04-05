package com.exrates.inout.configuration;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogginAspect {

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
        System.out.println("Response: " + retVal);
    }

}
