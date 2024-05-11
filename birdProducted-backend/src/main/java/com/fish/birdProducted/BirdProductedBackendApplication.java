package com.fish.birdProducted;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@MapperScan("com.fish.birdProducted.mapper")
@SpringBootApplication
@EnableMethodSecurity
public class BirdProductedBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BirdProductedBackendApplication.class, args);
    }

}
