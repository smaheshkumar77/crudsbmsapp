package com.example.crudsbmsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CrudSbmsAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrudSbmsAppApplication.class, args);
    }
}
