package com.jaworski.dbaccessservice;

import com.jaworski.dbaccessservice.repository.AccessRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DbAccessServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DbAccessServiceApplication.class, args);
        context.getBean(AccessRepository.class);
    }

}
