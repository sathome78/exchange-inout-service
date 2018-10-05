package com.exrates.inout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class InoutApplication {

    public static void main(String[] args) {
        SpringApplication.run(InoutApplication.class, args);
    }
}
