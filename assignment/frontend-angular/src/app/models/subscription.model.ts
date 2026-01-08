export interface Subscription {
    id?: number;
    userId: number;
    name: string;
    price: number;
    nextBillingDate: string; // ISO date format
    reminderTiming: string;  // '1_DAY', '1_HOUR', '30_MIN', 'CUSTOM'
    reminderCustomMinutes?: number;
    isActive: boolean;
    reminderEnabled: boolean;
    lastReminderSent?: string | null;
    createdAt?: string;
    updatedAt?: string;
}

export interface SubscriptionRequest {
    userId: number;
    name: string;
    price: number;
    nextBillingDate: string; // ISO date format
    reminderTiming?: string;
    reminderCustomMinutes?: number;
    isActive?: boolean;
    reminderEnabled?: boolean;
}
