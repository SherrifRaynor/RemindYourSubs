package com.remindyoursubs.subscriptionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SubscriptionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionServiceApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("  Subscription Service Started Successfully!");
        System.out.println("  Port: 8082");
        System.out.println("  Database: remindyoursubs_subscriptions");
        System.out.println("  Endpoints: /api/v1/subscriptions/**");
        System.out.println("===========================================\n");
    }
}
