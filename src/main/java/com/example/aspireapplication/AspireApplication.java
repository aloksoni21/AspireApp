package com.example.aspireapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class AspireApplication {

    public static void main(String[] args) {
        SpringApplication.run(AspireApplication.class, args);
    }

}
