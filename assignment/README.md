# RemindYourSubs - Microservices Architecture

A complete migration of RemindYourSubs from React to a microservices architecture using **Spring Boot** backend and **Angular 14** frontend with **Tailwind CSS**.

## 📋 Overview

This project demonstrates a microservices-based architecture for a subscription reminder and tracking application following REST API design principles.

### Technology Stack

**Backend:**
- Java 25 (JDK 25.0.1)
- Spring Boot 3.4.x
- Spring Cloud Gateway
- MySQL 8.x
- Maven 3.9.12

**Frontend:**
- Angular 14.0.7
- TypeScript 4.6.2
- Tailwind CSS 3.x
- Node.js 20.x

## 🏗️ Architecture

### Microservices

1. **API Gateway** (Port 8080)
   - Single entry point for all client requests
   - Routes to appropriate services
   - CORS configuration

2. **User Service** (Port 8081)
   - User profile management
   - Email configuration
   - Database: `remindyoursubs_users`

3. **Subscription Service** (Port 8082)
   - Subscription CRUD operations
   - Monthly expense calculation
   - Database: `remindyoursubs_subscriptions`

4. **Notification Service** (Port 8083)
   - Email reminder system using Resend API
   - Notification history
   - Database: `remindyoursubs_notifications`

### Database Design

Each microservice has its own MySQL database following microservices best practices:
- `remindyoursubs_users`
- `remindyoursubs_subscriptions`
- `remindyoursubs_notifications`

## 🚀 Setup Instructions

### Prerequisites

Ensure you have the following installed:
- JDK 25
- Maven 3.9.12
- Node.js 18+ & NPM
- Angular CLI 14
- MySQL 8.x (via Laragon)

### Database Setup

1. Start Laragon and ensure MySQL is running
2. Open HeidiSQL or your preferred MySQL client
3. Execute the database schema:

```bash
# Run the SQL script
mysql -u root < config/database-schema.sql
```

Or manually in HeidiSQL:
- Open `config/database-schema.sql`
- Execute the entire script

### Backend Setup

Each microservice needs to be started in the following order:

#### 1. API Gateway

```bash
cd assignment/backend-microservices/api-gateway
mvn clean install
mvn spring-boot:run
```

Verify: http://localhost:8080

#### 2. User Service

```bash
cd assignment/backend-microservices/user-service
mvn clean install
mvn spring-boot:run
```

Verify: http://localhost:8081/api/v1/users

#### 3. Subscription Service

```bash
cd assignment/backend-microservices/subscription-service
mvn clean install
mvn spring-boot:run
```

Verify: http://localhost:8082/api/v1/subscriptions

#### 4. Notification Service

```bash
cd assignment/backend-microservices/notification-service
mvn clean install
mvn spring-boot:run
```

Verify: http://localhost:8083/api/v1/notifications

### Frontend Setup

```bash
cd assignment/frontend-angular
npm install
ng serve
```

Access the application: http://localhost:4200

## 🧪 Testing

### API Testing with Postman

Postman collections are available in `docs/postman/`:
- User Service API Collection
- Subscription Service API Collection
- Notification Service API Collection
- Integration Tests Collection

Import these collections into Postman and run the test suites.

### Unit Tests

Backend unit tests:
```bash
# For each microservice
cd assignment/backend-microservices/[service-name]
mvn test
```

Frontend tests:
```bash
cd assignment/frontend-angular
ng test
```

## 📡 API Endpoints

### User Service (via API Gateway)

- `GET /api/v1/users` - Get all users
- `GET /api/v1/users/{id}` - Get user by ID
- `POST /api/v1/users` - Create user
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user

### Subscription Service (via API Gateway)

- `GET /api/v1/subscriptions` - Get all subscriptions
- `GET /api/v1/subscriptions/{id}` - Get subscription by ID
- `GET /api/v1/subscriptions/user/{userId}` - Get user's subscriptions
- `GET /api/v1/subscriptions/user/{userId}/expense` - Calculate monthly expense
- `POST /api/v1/subscriptions` - Create subscription
- `PUT /api/v1/subscriptions/{id}` - Update subscription
- `PUT /api/v1/subscriptions/{id}/toggle-reminder` - Toggle reminder
- `DELETE /api/v1/subscriptions/{id}` - Delete subscription

### Notification Service (via API Gateway)

- `POST /api/v1/notifications/send` - Send notification
- `POST /api/v1/notifications/check-reminders` - Check and send due reminders
- `GET /api/v1/notifications/user/{userId}` - Get user notifications
- `PUT /api/v1/notifications/{id}/read` - Mark as read

## 🔒 Configuration

### Backend Configuration

Each microservice has an `application.properties` file:

```properties
server.port=808X
spring.datasource.url=jdbc:mysql://localhost:3306/remindyoursubs_[service]
spring.datasource.username=root
spring.datasource.password=
```

### Frontend Configuration

Environment configuration in `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiGateway: 'http://localhost:8080'
};
```

## 📊 Port Summary

| Service | Port |
|---------|------|
| API Gateway | 8080 |
| User Service | 8081 |
| Subscription Service | 8082 |
| Notification Service | 8083 |
| Angular Frontend | 4200 |
| MySQL Database | 3306 |

## 📚 Documentation

Additional documentation is available in the `docs/` folder:
- Architecture diagrams
- Database ERD
- API documentation
- Screenshots

## 🎯 Features

- ✅ Subscription CRUD operations
- ✅ Monthly expense tracking
- ✅ Billing date reminders
- ✅ Email notifications via Resend API
- ✅ Visual status indicators
- ✅ Responsive UI with Tailwind CSS
- ✅ RESTful microservices architecture
- ✅ Separate databases per service

## 👨‍💻 Development

### Starting Development Environment

Use the provided script to start all services at once (recommended):

```bash
# Coming soon: startup script
```

Or start services manually in separate terminals as described in the Setup Instructions section.

## 📝 License

This is an academic project for demonstrating microservices architecture and REST API design principles.

## 🙏 Acknowledgments

Original React application: RemindYourSubs
Migrated to microservices architecture with Spring Boot and Angular 14.
