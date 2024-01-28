package com.softwaremind.todoappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TodoAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppBackendApplication.class, args);
    }
}
