import { calculateDaysUntilBilling } from '../utils/dateHelpers';
import { sendReminder } from './EmailService';

export async function checkAndSendReminders(subscriptions, config, updateSubscriptionCallback) {
    const { resendApiKey, userEmail, reminderDaysBefore } = config;
    const todayString = new Date().toISOString().split('T')[0];

    if (!resendApiKey || !userEmail) return;

    // We iterate through a copy or handle updates via callback
    // Since we need to update state, we'll collect updates
    const updates = [];

    for (const sub of subscriptions) {
        if (!sub.reminderEnabled || !sub.isActive) continue;

        const daysUntilBilling = calculateDaysUntilBilling(sub.billingDate);

        // Logic: Send if daysUntilBilling matches setting
        // Also check if daysUntilBilling is reasonable (e.g. not next month's 3 days before, but THE upcoming one)
        // Our calculateDaysUntilBilling returns positive days to next billing.

        // Using loose equality for days match
        if (daysUntilBilling === parseInt(reminderDaysBefore)) {
            // Check if already sent TODAY
            if (sub.lastReminderSent === todayString) continue;

            try {
                await sendReminder(sub, userEmail, resendApiKey);
                console.log(`Email sent for ${sub.name}`);

                updates.push({
                    ...sub,
                    lastReminderSent: todayString
                });
            } catch (error) {
                console.error(`Failed to send reminder for ${sub.name}:`, error);
                // We don't update lastReminderSent so it retries next check (or maybe backoff?)
                // For now, let's not update so it retries on next app load/check
            }
        }
    }

    if (updates.length > 0) {
        updateSubscriptionCallback(updates);
    }
}
