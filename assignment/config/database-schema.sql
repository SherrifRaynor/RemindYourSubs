-- ============================================
-- RemindYourSubs Microservices Database Schema
-- ============================================
-- Execute this script in MySQL to create all databases and tables
-- for the microservices architecture

-- ============================================
-- DATABASE CREATION
-- ============================================

-- User Service Database
CREATE DATABASE IF NOT EXISTS remindyoursubs_users 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Subscription Service Database
CREATE DATABASE IF NOT EXISTS remindyoursubs_subscriptions 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Notification Service Database
CREATE DATABASE IF NOT EXISTS remindyoursubs_notifications 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- ============================================
-- USER SERVICE SCHEMA
-- ============================================

USE remindyoursubs_users;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    reminder_days_before INT DEFAULT 3,
    resend_api_key VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- SUBSCRIPTION SERVICE SCHEMA
-- ============================================

USE remindyoursubs_subscriptions;

CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(15, 2) NOT NULL,
    billing_date INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    reminder_enabled BOOLEAN DEFAULT TRUE,
    last_reminder_sent DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_billing_date (billing_date),
    INDEX idx_active (is_active),
    CONSTRAINT chk_billing_date CHECK (billing_date >= 1 AND billing_date <= 31)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- NOTIFICATION SERVICE SCHEMA
-- ============================================

USE remindyoursubs_notifications;

CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    subscription_id BIGINT,
    type VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_subscription_id (subscription_id),
    INDEX idx_sent_at (sent_at),
    INDEX idx_is_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- SAMPLE DATA (Optional - for testing)
-- ============================================

-- Insert a test user
USE remindyoursubs_users;
INSERT INTO users (email, name, reminder_days_before) 
VALUES ('test@example.com', 'Test User', 3)
ON DUPLICATE KEY UPDATE email=email;

-- Get the user_id for sample subscriptions
SET @user_id = (SELECT id FROM users WHERE email = 'test@example.com' LIMIT 1);

-- Insert sample subscriptions
USE remindyoursubs_subscriptions;
INSERT INTO subscriptions (user_id, name, price, billing_date, is_active, reminder_enabled)
VALUES 
    (@user_id, 'Netflix', 49000.00, 15, TRUE, TRUE),
    (@user_id, 'Spotify', 54990.00, 10, TRUE, TRUE),
    (@user_id, 'YouTube Premium', 59000.00, 20, TRUE, TRUE);

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

-- Verify tables created
SELECT 'Users Table Count:' as Info, COUNT(*) as Count FROM remindyoursubs_users.users;
SELECT 'Subscriptions Table Count:' as Info, COUNT(*) as Count FROM remindyoursubs_subscriptions.subscriptions;
SELECT 'Notifications Table Count:' as Info, COUNT(*) as Count FROM remindyoursubs_notifications.notifications;
