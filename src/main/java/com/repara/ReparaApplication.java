package com.repara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReparaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReparaApplication.class, args);
        System.out.println("========================================");
        System.out.println("  🚀 RePara Backend iniciado");
        System.out.println("  📍 http://localhost:8080/api");
        System.out.println("  📚 http://localhost:8080/api/swagger-ui.html");
        System.out.println("========================================");
    }
}