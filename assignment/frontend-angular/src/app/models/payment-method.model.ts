export interface PaymentMethod {
    id?: number;
    userId: number;
    type: string; // 'CREDIT_CARD' | 'DEBIT_CARD' | 'E_WALLET' | 'BANK_ACCOUNT'
    provider?: string;
    maskedNumber?: string;
    nickname?: string;
    expiryDate?: string; // "12/2026"
    isDefault?: boolean;
    isActive?: boolean;
    isExpired?: boolean;
    isExpiringSoon?: boolean;
    daysUntilExpiry?: number;
    subscriptionCount?: number;
}

export interface PaymentMethodRequest {
    userId: number;
    type: string;
    provider?: string;
    lastFourDigits?: string;
    nickname?: string;
    expiryMonth?: number;
    expiryYear?: number;
    isDefault?: boolean;
}

export interface PaymentMethodAnalytics {
    totalMethods: number;
    activeMethods: number;
    expiringCount: number;
    expiredCount: number;
    subscriptionsByMethod: PaymentMethodDistribution[];
}

export interface PaymentMethodDistribution {
    paymentMethodId: number;
    nickname: string;
    maskedNumber: string;
    subscriptionCount: number;
    totalMonthlyAmount: number;
}
