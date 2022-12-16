package com.example.cbu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CbuApplication {
    public static void main(String[] args) {
        SpringApplication.run(CbuApplication.class, args);
    }
}
