package com.exrates.inout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {
        JpaRepositoriesAutoConfiguration.class,
        org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class
})
@EnableTransactionManagement
@EnableEurekaClient
public class InoutApplication {

    public static void main(String[] args) {
        SpringApplication.run(InoutApplication.class, args);
    }
}
