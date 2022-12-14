package com.example.cbu.config;

import com.example.cbu.service.UserService;
import com.example.cbu.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public static RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
