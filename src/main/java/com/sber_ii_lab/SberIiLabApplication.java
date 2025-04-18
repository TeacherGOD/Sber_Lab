package com.sber_ii_lab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class SberIiLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(SberIiLabApplication.class, args);
    }

}
