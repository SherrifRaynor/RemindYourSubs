-- ============================================
-- RemindYourSubs Single Database Schema
-- ============================================

CREATE DATABASE IF NOT EXISTS remindyoursubs 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE remindyoursubs;

-- ============================================
-- USERS TABLE
-- ============================================

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    reminder_days_before INT DEFAULT 3,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- SUBSCRIPTIONS TABLE
-- ============================================

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
    CONSTRAINT chk_billing_date CHECK (billing_date >= 1 AND billing_date <= 31),
    CONSTRAINT fk_subscriptions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- NOTIFICATIONS TABLE
-- ============================================

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
    INDEX idx_is_read (is_read),
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_notifications_subscription FOREIGN KEY (subscription_id) REFERENCES subscriptions(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- SAMPLE DATA (Optional - for testing)
-- ============================================

-- Insert a test user
INSERT INTO users (email, name, password, reminder_days_before) 
VALUES ('test@example.com', 'Test User', 'password', 3)
ON DUPLICATE KEY UPDATE email=email;

-- Get the user_id for sample subscriptions
SET @user_id = (SELECT id FROM users WHERE email = 'test@example.com' LIMIT 1);

-- Insert sample subscriptions
INSERT INTO subscriptions (user_id, name, price, billing_date, is_active, reminder_enabled)
VALUES 
    (@user_id, 'Netflix', 49000.00, 15, TRUE, TRUE),
    (@user_id, 'Spotify', 54990.00, 10, TRUE, TRUE),
    (@user_id, 'YouTube Premium', 59000.00, 20, TRUE, TRUE)
ON DUPLICATE KEY UPDATE name=name;

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

SELECT 'Users Count:' as Info, COUNT(*) as Count FROM users;
SELECT 'Subscriptions Count:' as Info, COUNT(*) as Count FROM subscriptions;
SELECT 'Notifications Count:' as Info, COUNT(*) as Count FROM notifications;
