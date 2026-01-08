import { Component, OnInit } from '@angular/core';
import { Subscription, SubscriptionRequest } from './models/subscription.model';
import { MonthlyExpense } from './models/monthly-expense.model';
import { AnalyticsData } from './models/analytics.model';
import { SubscriptionService } from './services/subscription.service';
import { ChartConfiguration } from 'chart.js';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  subscriptions: Subscription[] = [];
  monthlyExpense: MonthlyExpense | null = null;
  isFormOpen = false;
  editingSubscription: Subscription | null = null;

  formData: SubscriptionRequest = {
    userId: 1,
    name: '',
    price: 0,
    nextBillingDate: new Date(Date.now() + 86400000).toISOString().split('T')[0], // Tomorrow
    reminderTiming: '1_DAY'
  };

  toast = {
    show: false,
    message: '',
    type: 'success' as 'success' | 'error'
  };

  private userId = 1; // Hardcoded for single-user system

  // Analytics data and charts
  analyticsData: AnalyticsData | null = null;
  
  // Line Chart Config (Monthly Trend)
  lineChartData: ChartConfiguration['data'] = {
    datasets: [],
    labels: []
  };
  
  lineChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: true,
        labels: {
          color: '#e4e4e7' // zinc-200
        }
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          color: '#a1a1aa' // zinc-400
        },
        grid: {
          color: '#27272a' // zinc-800
        }
      },
      x: {
        ticks: {
          color: '#a1a1aa' // zinc-400
        },
        grid: {
          color: '#27272a' // zinc-800
        }
      }
    }
  };
  
  // Doughnut Chart Config (Distribution)
  doughnutChartData: ChartConfiguration<'doughnut'>['data'] = {
    datasets: [{
      data: [],
      backgroundColor: [
        '#3b82f6', // blue-500
        '#60a5fa', // blue-400
        '#93c5fd'  // blue-300
      ],
      borderColor: '#18181b', // zinc-900
      borderWidth: 2
    }],
    labels: []
  };
  
  doughnutChartOptions: ChartConfiguration<'doughnut'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: true,
        position: 'bottom',
        labels: {
          color: '#e4e4e7', // zinc-200
          padding: 15
        }
      }
    }
  };

  constructor(private subscriptionService: SubscriptionService) { }

  ngOnInit() {
    this.loadSubscriptions();
    this.loadMonthlyExpense();
    this.loadAnalytics();
  }

  loadSubscriptions() {
    this.subscriptionService.getSubscriptionsByUserId(this.userId).subscribe({
      next: (data) => {
        this.subscriptions = data;
      },
      error: (error) => {
        console.error('Error loading subscriptions:', error);
        this.showToast('Gagal memuat data langganan', 'error');
      }
    });
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

  loadAnalytics() {
    this.subscriptionService.getAnalytics(this.userId).subscribe({
      next: (data) => {
        this.analyticsData = data;
        this.updateCharts();
      },
      error: (error) => {
        console.error('Error loading analytics:', error);
      }
    });
  }

  updateCharts() {
    if (!this.analyticsData) return;

    // Update Line Chart (Monthly Trend)
    this.lineChartData = {
      labels: this.analyticsData.monthlyTrend.map(t => t.month),
      datasets: [
        {
          data: this.analyticsData.monthlyTrend.map(t => t.totalExpense),
          label: 'Pengeluaran Bulanan',
          borderColor: '#3b82f6', // blue-500
          backgroundColor: 'rgba(59, 130, 246, 0.1)',
          fill: true,
          tension: 0.4
        }
      ]
    };

    // Update Doughnut Chart (Distribution)
    const dist = this.analyticsData.distribution;
    this.doughnutChartData = {
      labels: ['< Rp 100k', 'Rp 100k - 250k', '> Rp 250k'],
      datasets: [{
        data: [dist.lowPrice, dist.mediumPrice, dist.highPrice],
        backgroundColor: [
          '#3b82f6', // blue-500
          '#60a5fa', // blue-400
          '#93c5fd'  // blue-300
        ],
        borderColor: '#18181b', // zinc-900
        borderWidth: 2
      }]
    };
  }

  openAddForm() {
    this.editingSubscription = null;
    this.isFormOpen = true;
  }

  openEditForm(subscription: Subscription) {
    this.editingSubscription = subscription;
    this.formData = {
      userId: this.userId,
      name: subscription.name,
      price: subscription.price,
      nextBillingDate: subscription.nextBillingDate,
      reminderTiming: subscription.reminderTiming || '1_DAY',
      reminderCustomMinutes: subscription.reminderCustomMinutes
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
      nextBillingDate: new Date(Date.now() + 86400000).toISOString().split('T')[0],
      reminderTiming: '1_DAY'
    };
  }

  handleSaveSubscription(formData: SubscriptionRequest) {
    if (this.editingSubscription && this.editingSubscription.id) {
      // Update existing
      this.subscriptionService.updateSubscription(this.editingSubscription.id, formData).subscribe({
        next: () => {
          this.showToast('Langganan berhasil diperbarui');
          this.loadSubscriptions();
          this.loadMonthlyExpense();
          this.loadAnalytics();
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
          this.loadAnalytics();
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
          this.loadAnalytics();
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

  calculateDaysUntilBilling(nextBillingDate: string): number {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const billing = new Date(nextBillingDate);
    billing.setHours(0, 0, 0, 0);
    
    const diffTime = billing.getTime() - today.getTime();
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
