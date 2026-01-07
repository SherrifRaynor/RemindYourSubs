package com.remindyoursubs.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("  Notification Service Started Successfully!");
        System.out.println("  Port: 8083");
        System.out.println("  Database: remindyoursubs_notifications");
        System.out.println("  Endpoints: /api/v1/notifications/**");
        System.out.println("  Email Provider: Resend API");
        System.out.println("===========================================\n");
    }
}
