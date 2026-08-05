package com.typemaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * TypeMaster Application Entry Point
 * Spring Boot Application class with Async and Scheduling support.
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class TypeMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TypeMasterApplication.class, args);
    }
}
