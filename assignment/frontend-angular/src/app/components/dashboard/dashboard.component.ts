import { Component, OnInit } from '@angular/core';
import { Subscription, SubscriptionRequest } from '../../models/subscription.model';
import { MonthlyExpense } from '../../models/monthly-expense.model';
import { SubscriptionService } from '../../services/subscription.service';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
    subscriptions: Subscription[] = [];
    monthlyExpense: MonthlyExpense | null = null;
    isFormOpen = false;
    editingSubscription: Subscription | null = null;
    currentUser: any;

    formData: SubscriptionRequest = {
        userId: 1,
        name: '',
        price: 0,
        billingDate: new Date().getDate()
    };

    toast = {
        show: false,
        message: '',
        type: 'success' as 'success' | 'error'
    };

    private userId = 1; // Will be updated from auth

    constructor(
        private subscriptionService: SubscriptionService,
        private authService: AuthService
    ) {
        this.currentUser = this.authService.currentUserValue;
        if (this.currentUser && this.currentUser.user && this.currentUser.user.id) {
            this.userId = this.currentUser.user.id;
            this.formData.userId = this.userId;
        }
    }

    ngOnInit() {
        this.loadSubscriptions();
        this.loadMonthlyExpense();
    }

    logout() {
        this.authService.logout();
    }

    upcomingPayments: Subscription[] = [];
    showNotifications = false;

    loadSubscriptions() {
        this.subscriptionService.getSubscriptionsByUserId(this.userId).subscribe({
            next: (data) => {
                this.subscriptions = data;
                this.checkUpcomingPayments();
            },
            error: (error) => {
                console.error('Error loading subscriptions:', error);
                this.showToast('Gagal memuat data langganan', 'error');
            }
        });
    }

    checkUpcomingPayments() {
        this.upcomingPayments = this.subscriptions.filter(sub => {
            const days = this.calculateDaysUntilBilling(sub.billingDate);
            return days >= 0 && days <= 7;
        });
    }

    toggleNotifications() {
        this.showNotifications = !this.showNotifications;
    }


    loadMonthlyExpense() {
        this.subscriptionService.getMonthlyExpense(this.userId).subscribe({
            next: (data) => {
                this.monthlyExpense = data;
            },
            error: (error) => {
                console.error('Error loading monthly expense:', error);
            }
        });
    }

    openAddForm() {
        this.editingSubscription = null;
        this.isFormOpen = true;
        this.formData = {
            userId: this.userId,
            name: '',
            price: 0,
            billingDate: new Date().getDate()
        };
    }

    openEditForm(subscription: Subscription) {
        this.editingSubscription = subscription;
        this.formData = {
            userId: this.userId,
            name: subscription.name,
            price: subscription.price,
            billingDate: subscription.billingDate
        };
        this.isFormOpen = true;
    }

    closeForm() {
        this.isFormOpen = false;
        this.editingSubscription = null;
        this.formData = {
            userId: this.userId,
            name: '',
            price: 0,
            billingDate: new Date().getDate()
        };
    }

    handleSaveSubscription(formData: SubscriptionRequest) {
        formData.userId = this.userId; // Ensure userId is correct
        if (this.editingSubscription && this.editingSubscription.id) {
            // Update existing
            this.subscriptionService.updateSubscription(this.editingSubscription.id, formData).subscribe({
                next: () => {
                    this.showToast('Langganan berhasil diperbarui');
                    this.loadSubscriptions();
                    this.loadMonthlyExpense();
                    this.closeForm();
                },
                error: (error) => {
                    console.error('Error updating subscription:', error);
                    this.showToast('Gagal memperbarui langganan', 'error');
                }
            });
        } else {
            // Create new
            this.subscriptionService.createSubscription(formData).subscribe({
                next: () => {
                    this.showToast('Langganan baru ditambahkan');
                    this.loadSubscriptions();
                    this.loadMonthlyExpense();
                    this.closeForm();
                },
                error: (error) => {
                    console.error('Error creating subscription:', error);
                    this.showToast('Gagal menambahkan langganan', 'error');
                }
            });
        }
    }

    handleDeleteSubscription(id: number) {
        if (confirm('Yakin ingin menghapus langganan ini?')) {
            this.subscriptionService.deleteSubscription(id).subscribe({
                next: () => {
                    this.showToast('Langganan dihapus');
                    this.loadSubscriptions();
                    this.loadMonthlyExpense();
                },
                error: (error) => {
                    console.error('Error deleting subscription:', error);
                    this.showToast('Gagal menghapus langganan', 'error');
                }
            });
        }
    }

    handleToggleReminder(id: number) {
        this.subscriptionService.toggleReminder(id).subscribe({
            next: () => {
                this.loadSubscriptions();
            },
            error: (error) => {
                console.error('Error toggling reminder:', error);
                this.showToast('Gagal mengubah pengingat', 'error');
            }
        });
    }

    showToast(message: string, type: 'success' | 'error' = 'success') {
        this.toast = { show: true, message, type };
        setTimeout(() => {
            this.toast.show = false;
        }, 3000);
    }

    calculateDaysUntilBilling(billingDate: number): number {
        const today = new Date();
        const currentDay = today.getDate();
        const currentMonth = today.getMonth();
        const currentYear = today.getFullYear();

        let nextBillingDate: Date;

        if (billingDate >= currentDay) {
            nextBillingDate = new Date(currentYear, currentMonth, billingDate);
        } else {
            nextBillingDate = new Date(currentYear, currentMonth + 1, billingDate);
        }

        const diffTime = nextBillingDate.getTime() - today.getTime();
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

        return diffDays;
    }

    getStatusColor(daysLeft: number): string {
        if (daysLeft <= 3) {
            return 'bg-red-900/30 text-red-400';
        } else if (daysLeft <= 7) {
            return 'bg-yellow-900/30 text-yellow-400';
        } else {
            return 'bg-green-900/30 text-green-400';
        }
    }

    getStatusLabel(daysLeft: number): string {
        if (daysLeft === 0) {
            return 'Hari Ini';
        } else if (daysLeft === 1) {
            return 'Besok';
        } else if (daysLeft < 0) {
            return 'Lewat';
        } else {
            return `${daysLeft} Hari`;
        }
    }
}
