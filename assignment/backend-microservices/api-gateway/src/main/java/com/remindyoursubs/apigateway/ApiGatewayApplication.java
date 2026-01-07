package com.remindyoursubs.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway Application
 * 
 * This service acts as the single entry point for all client requests.
 * It routes requests to the appropriate microservices:
 * - User Service (port 8081)
 * - Subscription Service (port 8082)
 * - Notification Service (port 8083)
 * 
 * It also handles CORS configuration for the Angular frontend.
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("  API Gateway Started Successfully!");
        System.out.println("  Port: 8080");
        System.out.println("  Routing to:");
        System.out.println("    - User Service: http://localhost:8081");
        System.out.println("    - Subscription Service: http://localhost:8082");
        System.out.println("    - Notification Service: http://localhost:8083");
        System.out.println("===========================================\n");
    }
}
