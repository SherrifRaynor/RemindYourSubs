import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentMethod, PaymentMethodRequest, PaymentMethodAnalytics } from '../models/payment-method.model';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class PaymentMethodService {
    private apiUrl = `${environment.apiUrl}/payment-methods`;

    constructor(private http: HttpClient) { }

    /**
     * Get all payment methods for a user
     */
    getUserPaymentMethods(userId: number): Observable<PaymentMethod[]> {
        return this.http.get<PaymentMethod[]>(`${this.apiUrl}/user/${userId}`);
    }

    /**
     * Get a single payment method by ID
     */
    getPaymentMethodById(id: number): Observable<PaymentMethod> {
        return this.http.get<PaymentMethod>(`${this.apiUrl}/${id}`);
    }

    /**
     * Create a new payment method
     */
    createPaymentMethod(request: PaymentMethodRequest): Observable<PaymentMethod> {
        return this.http.post<PaymentMethod>(this.apiUrl, request);
    }

    /**
     * Update an existing payment method
     */
    updatePaymentMethod(id: number, request: PaymentMethodRequest): Observable<PaymentMethod> {
        return this.http.put<PaymentMethod>(`${this.apiUrl}/${id}`, request);
    }

    /**
     * Delete a payment method
     */
    deletePaymentMethod(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    /**
     * Set a payment method as default
     */
    setAsDefault(id: number): Observable<PaymentMethod> {
        return this.http.put<PaymentMethod>(`${this.apiUrl}/${id}/set-default`, {});
    }

    /**
     * Get cards expiring within specified days
     */
    getExpiringCards(userId: number, days: number = 30): Observable<PaymentMethod[]> {
        return this.http.get<PaymentMethod[]>(`${this.apiUrl}/user/${userId}/expiring?days=${days}`);
    }

    /**
     * Get analytics for user's payment methods
     */
    getAnalytics(userId: number): Observable<PaymentMethodAnalytics> {
        return this.http.get<PaymentMethodAnalytics>(`${this.apiUrl}/user/${userId}/analytics`);
    }

    /**
     * Trigger expiry alert check
     */
    checkExpiryAlerts(userId: number): Observable<void> {
        return this.http.post<void>(`${this.apiUrl}/user/${userId}/check-alerts`, {});
    }
}
