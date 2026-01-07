package com.remindyoursubs.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * User Service Application
 * 
 * This service manages user profiles, email configurations,
 * and reminder preferences for the RemindYourSubs application.
 * 
 * Database: remindyoursubs_users
 * Port: 8081
 */
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("  User Service Started Successfully!");
        System.out.println("  Port: 8081");
        System.out.println("  Database: remindyoursubs_users");
        System.out.println("  Endpoints: /api/v1/users/**");
        System.out.println("===========================================\n");
    }
}
