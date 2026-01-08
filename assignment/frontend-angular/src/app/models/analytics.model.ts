export interface AnalyticsData {
    monthlyTrend: MonthlyTrendData[];
    upcomingBills: UpcomingBillData[];
    distribution: SubscriptionDistribution;
}

export interface MonthlyTrendData {
    month: string;
    totalExpense: number;
    subscriptionCount: number;
}

export interface UpcomingBillData {
    subscriptionId: number;
    subscriptionName: string;
    price: number;
    billingDate: number;
    daysUntil: number;
}

export interface SubscriptionDistribution {
    lowPrice: number;      // < 100k
    mediumPrice: number;   // 100k - 250k
    highPrice: number;     // > 250k
}
