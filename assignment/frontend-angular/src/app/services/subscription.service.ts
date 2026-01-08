import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Subscription, SubscriptionRequest } from '../models/subscription.model';
import { MonthlyExpense } from '../models/monthly-expense.model';
import { AnalyticsData } from '../models/analytics.model';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class SubscriptionService {
    private apiUrl = `${environment.apiUrl}/subscriptions`;

    constructor(private http: HttpClient) { }

    getAllSubscriptions(): Observable<Subscription[]> {
        return this.http.get<Subscription[]>(this.apiUrl);
    }

    getSubscriptionsByUserId(userId: number): Observable<Subscription[]> {
        return this.http.get<Subscription[]>(`${this.apiUrl}/user/${userId}`);
    }

    getSubscriptionById(id: number): Observable<Subscription> {
        return this.http.get<Subscription>(`${this.apiUrl}/${id}`);
    }

    getMonthlyExpense(userId: number): Observable<MonthlyExpense> {
        return this.http.get<MonthlyExpense>(`${this.apiUrl}/user/${userId}/expense`);
    }

    getAnalytics(userId: number): Observable<AnalyticsData> {
        return this.http.get<AnalyticsData>(`${this.apiUrl}/user/${userId}/analytics`);
    }

    createSubscription(subscription: SubscriptionRequest): Observable<Subscription> {
        return this.http.post<Subscription>(this.apiUrl, subscription);
    }

    updateSubscription(id: number, subscription: SubscriptionRequest): Observable<Subscription> {
        return this.http.put<Subscription>(`${this.apiUrl}/${id}`, subscription);
    }

    toggleReminder(id: number): Observable<Subscription> {
        return this.http.put<Subscription>(`${this.apiUrl}/${id}/toggle-reminder`, {});
    }

    deleteSubscription(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
