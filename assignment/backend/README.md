# RemindYourSubs - Simplified Backend

## 🚀 Overview

**Simplified monolithic Spring Boot application** - One service, one database, all features!

- **Port:** 8080
- **Database:** `remindyoursubs` (single database with 3 tables)
- **Architecture:** Monolithic REST API

## 📁 Project Structure

```
backend/
├── src/main/java/com/remindyoursubs/
│   ├── RemindYourSubsApplication.java    # Main application
│   ├── model/                             # Entities (User, Subscription, Notification)
│   ├── repository/                        # JPA Repositories
│   ├── service/                           # Business logic
│   ├── controller/                        # REST Controllers
│   ├── dto/                              # Data Transfer Objects
│   └── exception/                        # Exception handlers
├── src/main/resources/
│   └── application.properties            # Configuration
└── pom.xml                               # Maven dependencies
```

## 🛠️ Setup Instructions

### 1. Database Setup

Run the SQL script to create the database and tables:

```bash
# In HeidiSQL or MySQL command line
mysql -u root < config/database-schema-simple.sql
```

This creates:
- Database: `remindyoursubs`
- Tables: `users`, `subscriptions`, `notifications`

### 2. Start the Application

```bash
cd assignment/backend
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

## 📡 API Endpoints

### Users API
- `GET /api/v1/users` - Get all users
- `GET /api/v1/users/{id}` - Get user by ID
- `POST /api/v1/users` - Create user
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

### Subscriptions API
- `GET /api/v1/subscriptions` - Get all subscriptions
- `GET /api/v1/subscriptions/{id}` - Get subscription by ID
- `GET /api/v1/subscriptions/user/{userId}` - Get user's subscriptions
- `GET /api/v1/subscriptions/user/{userId}/expense` - Calculate monthly expense
- `POST /api/v1/subscriptions` - Create subscription
- `PUT /api/v1/subscriptions/{id}` - Update subscription
- `PUT /api/v1/subscriptions/{id}/toggle-reminder` - Toggle reminder
- `DELETE /api/v1/subscriptions/{id}` - Delete subscription

### Notifications API
- `POST /api/v1/notifications/send` - Send email notification (requires X-API-KEY header)
- `GET /api/v1/notifications/user/{userId}` - Get user notifications
- `GET /api/v1/notifications/user/{userId}/unread` - Get unread notifications
- `PUT /api/v1/notifications/{id}/read` - Mark notification as read

## 🧪 Testing with cURL

### Create a User
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "name": "Test User",
    "reminderDaysBefore": 3
  }'
```

### Create a Subscription
```bash
curl -X POST http://localhost:8080/api/v1/subscriptions \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "name": "Netflix",
    "price": 49000,
    "billingDate": 15
  }'
```

### Get Monthly Expense
```bash
curl http://localhost:8080/api/v1/subscriptions/user/1/expense
```

## 🎯 Features

✅ User management with email validation
✅ Subscription CRUD operations
✅ Monthly expense calculation
✅ Email notifications via Resend API
✅ Reminder toggle functionality
✅ CORS enabled for Angular frontend (localhost:4200)
✅ Global exception handling
✅ Input validation
✅ Foreign key relationships in database

## 💾 Database Schema

```sql
users
- id (PK)
- email (UNIQUE)
- name
- reminder_days_before
- resend_api_key
- created_at, updated_at

subscriptions
- id (PK)
- user_id (FK → users.id)
- name
- price
- billing_date (1-31)
- is_active
- reminder_enabled
- last_reminder_sent
- created_at, updated_at

notifications
- id (PK)
- user_id (FK → users.id)
- subscription_id (FK → subscriptions.id)
- type
- message
- sent_at
- is_read
- created_at
```

## 🔧 Configuration

Edit `application.properties` for:
- Database connection (default: localhost:3306/remindyoursubs)
- Server port (default: 8080)
- CORS origins
- Resend API URL

## ✨ Benefits of This Simplified Approach

- **Simpler Deployment:** One application to deploy instead of 4
- **Easier Development:** No need to coordinate between services
- **Single Database:** Simpler transactions and queries
- **Built-in CORS:** No need for separate API Gateway
- **Perfect for Learning:** Focus on Spring Boot fundamentals
- **Still Production-Ready:** Includes validation, error handling, and proper REST design

## 🚦 Next Steps

1. ✅ Run the SQL script to create the database
2. ✅ Start the backend with `mvn spring-boot:run`
3. ⏳ Test endpoints with Postman or cURL
4. ⏳ Create Angular 14 frontend
5. ⏳ Connect frontend to backend APIs
