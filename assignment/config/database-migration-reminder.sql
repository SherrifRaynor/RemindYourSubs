-- Migration script to update billing date from day-of-month to full date
-- and add reminder timing fields

-- Step 1: Add new columns (only if they don't exist)
-- Check and add next_billing_date
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
AND TABLE_NAME = 'subscriptions' 
AND COLUMN_NAME = 'next_billing_date';

SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE subscriptions ADD COLUMN next_billing_date DATE', 
    'SELECT "Column next_billing_date already exists" AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add reminder_custom_minutes
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
AND TABLE_NAME = 'subscriptions' 
AND COLUMN_NAME = 'reminder_custom_minutes';

SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE subscriptions ADD COLUMN reminder_custom_minutes INT', 
    'SELECT "Column reminder_custom_minutes already exists" AS message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 2: Migrate existing data (only if billing_date exists and next_billing_date is null)
UPDATE subscriptions 
SET next_billing_date = CASE 
    WHEN billing_date >= DAY(CURRENT_DATE)
    THEN DATE_FORMAT(CONCAT(YEAR(CURRENT_DATE), '-', MONTH(CURRENT_DATE), '-', billing_date), '%Y-%m-%d')
    ELSE DATE_FORMAT(CONCAT(YEAR(DATE_ADD(CURRENT_DATE, INTERVAL 1 MONTH)), '-', MONTH(DATE_ADD(CURRENT_DATE, INTERVAL 1 MONTH)), '-', billing_date), '%Y-%m-%d')
END
WHERE billing_date IS NOT NULL 
AND next_billing_date IS NULL;

-- Set default reminder_timing for existing records (if null)
UPDATE subscriptions 
SET reminder_timing = '1_DAY'
WHERE reminder_timing IS NULL;

-- Step 3: Drop old billing_date column (OPTIONAL - uncomment when ready)
-- Backup your data first!
-- ALTER TABLE subscriptions DROP COLUMN billing_date;

-- Verification query
SELECT id, name, next_billing_date, reminder_timing, reminder_custom_minutes 
FROM subscriptions 
LIMIT 10;
