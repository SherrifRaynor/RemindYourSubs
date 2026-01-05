import React from 'react';
import { Pencil, Trash2, Bell, BellOff } from 'lucide-react';
import StatusBadge from './StatusBadge';
import { calculateDaysUntilBilling } from '../utils/dateHelpers';

export default function SubscriptionCard({ subscription, onEdit, onDelete, onToggleReminder }) {
    const daysLeft = calculateDaysUntilBilling(subscription.billingDate);

    return (
        <div className="bg-zinc-800 rounded-lg p-6 shadow-lg border border-zinc-700 hover:border-zinc-600 transition-colors group">
            <div className="flex justify-between items-start mb-4">
                <div>
                    <h3 className="text-xl font-bold text-white mb-1 group-hover:text-blue-400 transition-colors">
                        {subscription.name}
                    </h3>
                    <p className="text-2xl font-semibold text-gray-200">
                        Rp {parseInt(subscription.price).toLocaleString('id-ID')}
                        <span className="text-sm text-gray-500 font-normal"> / bulan</span>
                    </p>
                </div>
                <StatusBadge daysLeft={daysLeft} />
            </div>

            <div className="flex justify-between items-end mt-4">
                <div className="text-sm text-gray-400">
                    <p>Tagihan setiap tgl <span className="text-white font-medium">{subscription.billingDate}</span></p>
                    <div className="flex items-center mt-2 space-x-2">
                        <button
                            onClick={() => onToggleReminder(subscription.id)}
                            className={`flex items-center text-xs font-medium px-2 py-1 rounded transition-colors ${subscription.reminderEnabled
                                    ? 'bg-green-900/30 text-green-400 hover:bg-green-900/50'
                                    : 'bg-zinc-700 text-gray-400 hover:bg-zinc-600'
                                }`}
                        >
                            {subscription.reminderEnabled ? <Bell className="w-3 h-3 mr-1" /> : <BellOff className="w-3 h-3 mr-1" />}
                            {subscription.reminderEnabled ? 'Reminder ON' : 'Reminder OFF'}
                        </button>
                    </div>
                </div>

                <div className="flex space-x-2">
                    <button
                        onClick={() => onEdit(subscription)}
                        className="p-2 text-gray-400 hover:text-white hover:bg-zinc-700 rounded-lg transition-colors"
                        title="Edit"
                    >
                        <Pencil className="w-5 h-5" />
                    </button>
                    <button
                        onClick={() => onDelete(subscription.id)}
                        className="p-2 text-gray-400 hover:text-red-400 hover:bg-red-900/20 rounded-lg transition-colors"
                        title="Delete"
                    >
                        <Trash2 className="w-5 h-5" />
                    </button>
                </div>
            </div>
        </div>
    );
}
