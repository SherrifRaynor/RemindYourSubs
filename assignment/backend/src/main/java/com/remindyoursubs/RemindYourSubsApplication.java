package com.remindyoursubs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * RemindYourSubs Backend Application
 * 
 * Monolithic Spring Boot application with all features:
 * - User management
 * - Subscription CRUD
 * - Notification system with Resend API
 * 
 * Port: 8080
 * Database: remindyoursubs (single database with multiple tables)
 */
@SpringBootApplication
public class RemindYourSubsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemindYourSubsApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("  RemindYourSubs Backend Started!");
        System.out.println("  Port: 8080");
        System.out.println("  Database: remindyoursubs");
        System.out.println("  API Endpoints:");
        System.out.println("    - /api/v1/users/**");
        System.out.println("    - /api/v1/subscriptions/**");
        System.out.println("    - /api/v1/notifications/**");
        System.out.println("===========================================\n");
    }

    /**
     * CORS Configuration for Angular frontend
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
