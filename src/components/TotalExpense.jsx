import React from 'react';

export default function TotalExpense({ subscriptions }) {
    const total = subscriptions
        .filter(sub => sub.isActive)
        .reduce((sum, sub) => sum + Number(sub.price), 0);

    return (
        <div className="bg-zinc-800 rounded-lg p-6 shadow-lg border border-zinc-700 flex flex-col items-center justify-center mb-8">
            <h3 className="text-gray-400 text-sm font-medium uppercase tracking-wider mb-2">Total Pengeluaran Bulanan</h3>
            <div className="text-4xl font-bold text-white">
                Rp {total.toLocaleString('id-ID')}
            </div>
        </div>
    );
}
