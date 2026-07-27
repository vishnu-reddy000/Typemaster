package com.typemaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * TypeMaster Application Entry Point
 * Spring Boot Application class.
 */
@SpringBootApplication
@EnableScheduling
public class TypeMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TypeMasterApplication.class, args);
    }
}
