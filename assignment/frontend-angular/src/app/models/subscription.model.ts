export interface Subscription {
    id?: number;
    userId: number;
    name: string;
    price: number;
    billingDate: number;
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
    billingDate: number;
    isActive?: boolean;
    reminderEnabled?: boolean;
}
