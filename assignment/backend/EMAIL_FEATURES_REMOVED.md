# Removed Email Notification Features

## Changes Made

### 1. Database Schema
**File:** `config/database-schema-simple.sql`

Removed column:
```sql
-- REMOVED: resend_api_key VARCHAR(255),
```

**Users table now:**
- id (PK)
- email
- name
- reminder_days_before
- created_at
- updated_at

### 2. Backend Entity
**File:** `backend/src/main/java/com/remindyoursubs/model/User.java`

Removed field:
```java
// REMOVED: private String resendApiKey;
```

### 3. Backend DTOs
**Files:**
- `UserRequestDTO.java` - Removed `resendApiKey` field
- `UserResponseDTO.java` - Removed `resendApiKey` field

### 4. Backend Service
**File:** `service/UserService.java`

Removed all references to `resendApiKey` in:
- `createUser()` method
- `updateUser()` method
- `convertToDTO()` method

### 5. Frontend Models
**File:** `frontend-angular/src/app/models/user.model.ts`

Removed `resendApiKey` from:
- `UserConfig` interface
- `UserRequest` interface

## Features Temporarily Disabled

- ❌ Email notifications via Resend API
- ❌ Resend API key configuration
- ❌ `NotificationService` (will be implemented later)
- ❌ Email reminder sending

## Features Still Working

- ✅ User management (CRUD)
- ✅ Subscription management (CRUD)
- ✅ Monthly expense calculation
- ✅ Reminder toggle (backend only, no email sent)
- ✅ Notification history (backend structure remains)

## To Re-enable Later

1. Add `resend_api_key` column back to database
2. Add field back to User entity and DTOs
3. Implement NotificationService
4. Connect email sending functionality

## Database Migration

If you already created the database with `resend_api_key`, run:

```sql
ALTER TABLE users DROP COLUMN resend_api_key;
```

Or recreate the database:
```bash
mysql -u root < assignment/config/database-schema-simple.sql
```

## Status

✅ **Email features removed**
✅ **Backend compiles successfully**
✅ **Frontend models updated**
✅ **Ready for basic CRUD operations**
