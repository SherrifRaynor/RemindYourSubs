import { Component, OnInit } from '@angular/core';
import { PaymentMethod, PaymentMethodRequest } from '../../models/payment-method.model';
import { PaymentMethodService } from '../../services/payment-method.service';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-payment-methods',
    templateUrl: './payment-methods.component.html',
    styleUrls: ['./payment-methods.component.css']
})
export class PaymentMethodsComponent implements OnInit {
    paymentMethods: PaymentMethod[] = [];
    loading = true;
    error = '';
    showForm = false;
    editingMethod: PaymentMethod | null = null;
    currentUserId: number = 0;

    // Form model
    formData: PaymentMethodRequest = {
        userId: 0,
        type: 'CREDIT_CARD',
        provider: '',
        lastFourDigits: '',
        nickname: '',
        expiryMonth: undefined,
        expiryYear: undefined,
        isDefault: false
    };

    paymentTypes = [
        { value: 'CREDIT_CARD', label: 'Credit Card', icon: '💳' },
        { value: 'DEBIT_CARD', label: 'Debit Card', icon: '💳' },
        { value: 'E_WALLET', label: 'E-Wallet', icon: '📱' },
        { value: 'BANK_ACCOUNT', label: 'Bank Account', icon: '🏦' }
    ];

    providers = ['Visa', 'Mastercard', 'American Express', 'Discover', 'GoPay', 'OVO', 'DANA', 'ShopeePay', 'BCA', 'Mandiri', 'BNI', 'BRI'];

    constructor(
        private paymentMethodService: PaymentMethodService,
        private authService: AuthService
    ) { }

    ngOnInit(): void {
        const user = this.authService.currentUserValue;
        if (user && user.user) {
            this.currentUserId = user.user.id;
            this.formData.userId = user.user.id;
            this.loadPaymentMethods();
        }
    }

    loadPaymentMethods(): void {
        this.loading = true;
        this.error = '';

        this.paymentMethodService.getUserPaymentMethods(this.currentUserId).subscribe({
            next: (methods) => {
                this.paymentMethods = methods;
                this.loading = false;
            },
            error: (err) => {
                this.error = 'Failed to load payment methods';
                this.loading = false;
                console.error('Error loading payment methods:', err);
            }
        });
    }

    openCreateForm(): void {
        this.editingMethod = null;
        this.formData = {
            userId: this.currentUserId,
            type: 'CREDIT_CARD',
            provider: '',
            lastFourDigits: '',
            nickname: '',
            expiryMonth: undefined,
            expiryYear: undefined,
            isDefault: false
        };
        this.showForm = true;
    }

    openEditForm(method: PaymentMethod): void {
        this.editingMethod = method;
        this.formData = {
            userId: this.currentUserId,
            type: method.type,
            provider: method.provider || '',
            lastFourDigits: method.maskedNumber?.slice(-4) || '',
            nickname: method.nickname || '',
            expiryMonth: method.expiryDate ? parseInt(method.expiryDate.split('/')[0]) : undefined,
            expiryYear: method.expiryDate ? parseInt(method.expiryDate.split('/')[1]) : undefined,
            isDefault: method.isDefault || false
        };
        this.showForm = true;
    }

    closeForm(): void {
        this.showForm = false;
        this.editingMethod = null;
    }

    savePaymentMethod(): void {
        if (this.isFormValid()) {
            if (this.editingMethod) {
                // Update existing
                this.paymentMethodService.updatePaymentMethod(this.editingMethod.id!, this.formData).subscribe({
                    next: () => {
                        this.loadPaymentMethods();
                        this.closeForm();
                    },
                    error: (err) => {
                        this.error = 'Failed to update payment method';
                        console.error('Error:', err);
                    }
                });
            } else {
                // Create new
                this.paymentMethodService.createPaymentMethod(this.formData).subscribe({
                    next: () => {
                        this.loadPaymentMethods();
                        this.closeForm();
                    },
                    error: (err) => {
                        this.error = 'Failed to create payment method';
                        console.error('Error:', err);
                    }
                });
            }
        }
    }

    deletePaymentMethod(method: PaymentMethod): void {
        if (confirm(`Delete ${method.nickname || method.provider}?`)) {
            this.paymentMethodService.deletePaymentMethod(method.id!).subscribe({
                next: () => {
                    this.loadPaymentMethods();
                },
                error: (err) => {
                    this.error = 'Failed to delete payment method';
                    console.error('Error:', err);
                }
            });
        }
    }

    setAsDefault(method: PaymentMethod): void {
        this.paymentMethodService.setAsDefault(method.id!).subscribe({
            next: () => {
                this.loadPaymentMethods();
            },
            error: (err) => {
                this.error = 'Failed to set as default';
                console.error('Error:', err);
            }
        });
    }

    getIcon(type: string): string {
        const typeData = this.paymentTypes.find(t => t.value === type);
        return typeData ? typeData.icon : '💳';
    }

    getStatusClass(method: PaymentMethod): string {
        if (method.isExpired) return 'text-red-500';
        if (method.isExpiringSoon) return 'text-yellow-500';
        return 'text-green-500';
    }

    getStatusText(method: PaymentMethod): string {
        if (method.isExpired) return 'Expired';
        if (method.isExpiringSoon) return `Expires in ${method.daysUntilExpiry} days`;
        return 'Active';
    }

    isFormValid(): boolean {
        if (!this.formData.type || !this.formData.nickname) return false;

        if (this.formData.type === 'CREDIT_CARD' || this.formData.type === 'DEBIT_CARD') {
            return !!(this.formData.lastFourDigits && this.formData.expiryMonth && this.formData.expiryYear);
        }

        return true;
    }

    needsExpiry(): boolean {
        return this.formData.type === 'CREDIT_CARD' || this.formData.type === 'DEBIT_CARD';
    }
}
