# RemindYourSubs - Quick Start Guide

## Prerequisites
- Java 17 or higher
- Node.js 14+ and npm
- MySQL 8.0+
- Maven 3.6+

## 1️⃣ Database Setup

```bash
# Open MySQL client (HeidiSQL, Workbench, or command line)
# Execute the complete setup file:
SOURCE assignment/config/database-complete-setup.sql;
```

This will create:
- Database: `remindyoursubs`
- Tables: `users`, `subscriptions`, `notifications`, `payment_methods`, `payment_alerts`

## 2️⃣ Backend Setup (Spring Boot)

```bash
cd assignment/backend

# Build the project
mvn clean install

# Run the backend server
mvn spring-boot:run
```

Backend will run on: **http://localhost:8080**

## 3️⃣ Frontend Setup (Angular)

```bash
cd assignment/frontend-angular

# Install dependencies
npm install

# Run development server
ng serve
```

Frontend will run on: **http://localhost:4200**

---

## 📡 API Endpoints

### 🔐 Authentication
```
POST   /api/v1/auth/register          - Register new user
POST   /api/v1/auth/login             - Login user
```

### 👤 Users
```
GET    /api/v1/users                  - Get all users
GET    /api/v1/users/{id}             - Get user by ID
POST   /api/v1/users                  - Create user
PUT    /api/v1/users/{id}             - Update user
DELETE /api/v1/users/{id}             - Delete user
```

### 📅 Subscriptions
```
GET    /api/v1/subscriptions                          - Get all subscriptions
GET    /api/v1/subscriptions/{id}                     - Get subscription by ID
GET    /api/v1/subscriptions/user/{userId}            - Get user's subscriptions
POST   /api/v1/subscriptions                          - Create subscription
PUT    /api/v1/subscriptions/{id}                     - Update subscription
DELETE /api/v1/subscriptions/{id}                     - Delete subscription
PUT    /api/v1/subscriptions/{id}/toggle-reminder     - Toggle reminder on/off
GET    /api/v1/subscriptions/user/{userId}/monthly-expense  - Get monthly expense
GET    /api/v1/subscriptions/user/{userId}/analytics        - Get analytics
```

### 💳 Payment Methods
```
GET    /api/v1/payment-methods/user/{userId}          - Get user's payment methods
GET    /api/v1/payment-methods/{id}                   - Get payment method by ID
POST   /api/v1/payment-methods                        - Create payment method
PUT    /api/v1/payment-methods/{id}                   - Update payment method
DELETE /api/v1/payment-methods/{id}                   - Delete payment method
PUT    /api/v1/payment-methods/{id}/set-default       - Set as default
GET    /api/v1/payment-methods/user/{userId}/expiring?days=30  - Get expiring cards
GET    /api/v1/payment-methods/user/{userId}/analytics        - Get analytics
POST   /api/v1/payment-methods/user/{userId}/check-alerts     - Trigger expiry alerts
```
---

## 🧪 Testing with Postman

### Example: Register User
```json
POST http://localhost:8080/api/v1/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "name": "John Doe",
  "password": "password123",
  "reminderDaysBefore": 3
}
```

### Example: Create Subscription
```json
POST http://localhost:8080/api/v1/subscriptions
Content-Type: application/json

{
  "userId": 1,
  "name": "Netflix Premium",
  "price": 186000,
  "nextBillingDate": "2026-02-15",
  "reminderTiming": "1_DAY",
  "paymentMethodId": 1
}
```

### Example: Create Payment Method
```json
POST http://localhost:8080/api/v1/payment-methods
Content-Type: application/json

{
  "userId": 1,
  "type": "CREDIT_CARD",
  "provider": "Visa",
  "lastFourDigits": "4532",
  "nickname": "Personal Visa",
  "expiryMonth": 12,
  "expiryYear": 2026,
  "isDefault": true
}
```

---

## 🚀 Quick Access

- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080
- **API Base Path**: `/api/v1`

---

## 🛠️ Troubleshooting

**Backend won't start?**
- Check Java version: `java -version` (must be 17+)
- Ensure MySQL is running
- Verify database exists: `SHOW DATABASES;`

**Frontend errors?**
- Delete `node_modules` and run `npm install` again
- Clear Angular cache: `ng cache clean`

**Database connection failed?**
- Check `application.properties` for correct MySQL credentials
- Default: `root` user with no password

---

**Project is ready to run!** 🎉
