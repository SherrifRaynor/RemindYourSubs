# Backend Microservices Implementation Summary

This document provides an overview of the completed backend microservices for the RemindYourSubs application.

## Architecture Overview

The backend consists of 4 Spring Boot microservices communicating via REST APIs:

```
┌──────────────────────────────────────────────────────────┐
│                     API Gateway (8080)                    │
│              Single Entry Point + CORS Config             │
└────────────────────┬─────────────────────────────────────┘
                    │
        ┌───────────┼───────────┐
        │           │           │
        ▼           ▼           ▼
┌───────────┐ ┌─────────────┐ ┌──────────────┐
│   User    │ │Subscription │ │Notification  │
│  Service  │ │   Service   │ │   Service    │
│  (8081)   │ │   (8082)    │ │   (8083)     │
└─────┬─────┘ └──────┬──────┘ └──────┬───────┘
      │              │                │
      ▼              ▼                ▼
┌────────────┐ ┌─────────────┐  ┌─────────────┐
│ users DB   │ │ subscrip... │  │notifica...  │
└────────────┘ └──────────────┘ └─────────────┘
```

## Microservices Details

### 1. API Gateway (Port 8080)

**Technology:** Spring Cloud Gateway
**Purpose:** Routes requests to appropriate microservices and handles CORS

**Routes:**
- `/api/v1/users/**` → User Service (8081)
- `/api/v1/subscriptions/**` → Subscription Service (8082)
- `/api/v1/notifications/**` → Notification Service (8083)

**Files Created:**
- `pom.xml` - Maven dependencies
- `application.yml` - Routing and CORS configuration
- `ApiGatewayApplication.java` - Main application class

---

### 2. User Service (Port 8081)

**Database:** `remindyoursubs_users`
**Purpose:** Manage user profiles and email configurations

**Endpoints:**
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users` | Get all users |
| GET | `/api/v1/users/{id}` | Get user by ID |
| GET | `/api/v1/users/email/{email}` | Get user by email |
| POST | `/api/v1/users` | Create new user |
| PUT | `/api/v1/users/{id}` | Update user |
| DELETE | `/api/v1/users/{id}` | Delete user |

**Key Features:**
- Email uniqueness validation
- Reminder preferences storage
- Resend API key storage

**Files Created:**
- Model: `User.java`
- DTOs: `UserRequestDTO.java`, `UserResponseDTO.java`
- Repository: `UserRepository.java`
- Service: `UserService.java`, `UserServiceImpl.java`
- Controller: `UserController.java`
- Exception: `ResourceNotFoundException.java`, `GlobalExceptionHandler.java`

---

### 3. Subscription Service (Port 8082)

**Database:** `remindyoursubs_subscriptions`
**Purpose:** Manage subscriptions and calculate monthly expenses

**Endpoints:**
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/subscriptions` | Get all subscriptions |
| GET | `/api/v1/subscriptions/{id}` | Get subscription by ID |
| GET | `/api/v1/subscriptions/user/{userId}` | Get user's subscriptions |
| GET | `/api/v1/subscriptions/user/{userId}/expense` | Calculate monthly expense |
| POST | `/api/v1/subscriptions` | Create subscription |
| PUT | `/api/v1/subscriptions/{id}` | Update subscription |
| PUT | `/api/v1/subscriptions/{id}/toggle-reminder` | Toggle reminder |
| DELETE | `/api/v1/subscriptions/{id}` | Delete subscription |

**Key Features:**
- Monthly expense calculation using SQL aggregation
- Reminder toggle functionality
- Billing date validation (1-31)
- Active/inactive subscription tracking

**Files Created:**
- Model: `Subscription.java`
- DTOs: `SubscriptionRequestDTO.java`, `SubscriptionResponseDTO.java`, `MonthlyExpenseDTO.java`
- Repository: `SubscriptionRepository.java` (with custom expense query)
- Service: `SubscriptionService.java`, `SubscriptionServiceImpl.java`
- Controller: `SubscriptionController.java`
- Exception: `ResourceNotFoundException.java`, `GlobalExceptionHandler.java`

---

### 4. Notification Service (Port 8083)

**Database:** `remindyoursubs_notifications`
**Purpose:** Send email reminders and track notification history

**Endpoints:**
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/notifications/send` | Send email notification |
| GET | `/api/v1/notifications/user/{userId}` | Get user notifications |
| GET | `/api/v1/notifications/user/{userId}/unread` | Get unread notifications |
| PUT | `/api/v1/notifications/{id}/read` | Mark as read |

**Key Features:**
- Resend API integration for email sending
- HTML email template generation
- Notification history tracking
- Read/unread status management

**Files Created:**
- Model: `Notification.java`
- DTOs: `EmailRequestDTO.java`, `NotificationResponseDTO.java`
- Repository: `NotificationRepository.java`
- Service: `NotificationService.java`, `EmailService.java`
- Controller: `NotificationController.java`

---

## Technology Stack

**Java:** 25 (JDK 25.0.1)
**Spring Boot:** 3.4.1
**Build Tool:** Maven 3.9.12
**Database:** MySQL 8.x
**Dependencies:**
- Spring Web
- Spring Data JPA
- Spring Cloud Gateway (API Gateway only)
- MySQL Connector/J
- Lombok
- Spring Validation
- Spring WebFlux (for HTTP clients)
- Spring Mail (Notification Service)
- DevTools

## Database Schema

All databases use UTF8MB4 character set with Unicode collation.

**remindyoursubs_users:**
- users table (id, email, name, reminder_days_before, resend_api_key, timestamps)

**remindyoursubs_subscriptions:**
- subscriptions table (id, user_id, name, price, billing_date, is_active, reminder_enabled, last_reminder_sent, timestamps)

**remindyoursubs_notifications:**
- notifications table (id, user_id, subscription_id, type, message, sent_at, is_read, created_at)

## Next Steps

1. ✅ All 4 microservices created
2. ⏳ Create Angular 14 frontend
3. ⏳ Test services with Postman
4. ⏳ Run and verify integration
5. ⏳ Create documentation and diagrams
