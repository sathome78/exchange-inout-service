package com.exrates.inout;

import com.exrates.inout.configuration.SpringPropertyConfigLog4j2;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {
        JpaRepositoriesAutoConfiguration.class,
        org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class
})
@EnableTransactionManagement
@EnableScheduling
@EnableRabbit
public class InoutApplication  {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(InoutApplication.class);
        application.addListeners(new SpringPropertyConfigLog4j2());
        application.run(args);
    }

    @Scheduled(cron = "0 * * * * *")
    public void print(){
        System.out.print("Is alive! ");
    }

}
