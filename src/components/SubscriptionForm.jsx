import React, { useState, useEffect } from 'react';
import { X, Save } from 'lucide-react';

export default function SubscriptionForm({ initialData = null, onSave, onCancel }) {
    const [formData, setFormData] = useState({
        name: '',
        price: '',
        billingDate: new Date().getDate(),
    });

    useEffect(() => {
        if (initialData) {
            setFormData({
                name: initialData.name,
                price: initialData.price,
                billingDate: initialData.billingDate,
            });
        }
    }, [initialData]);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!formData.name || !formData.price || !formData.billingDate) return;

        onSave({
            ...formData,
            price: Number(formData.price),
            billingDate: Number(formData.billingDate),
        });
    };

    return (
        <div className="bg-zinc-800 rounded-lg p-6 shadow-lg border border-zinc-700 mb-8 animate-fade-in">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-xl font-bold text-white">
                    {initialData ? 'Edit Langganan' : 'Tambah Langganan Baru'}
                </h2>
                <button onClick={onCancel} className="text-gray-400 hover:text-white">
                    <X className="w-6 h-6" />
                </button>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-400 mb-1">Nama Layanan</label>
                    <input
                        type="text"
                        placeholder="Contoh: Netflix Premium"
                        value={formData.name}
                        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                        className="w-full bg-zinc-900 border border-zinc-700 rounded-lg px-4 py-3 text-white focus:ring-2 focus:ring-blue-600 focus:border-transparent outline-none transition-all placeholder-gray-600"
                        required
                    />
                </div>

                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-400 mb-1">Harga (Rp)</label>
                        <input
                            type="number"
                            placeholder="186000"
                            value={formData.price}
                            onChange={(e) => setFormData({ ...formData, price: e.target.value })}
                            className="w-full bg-zinc-900 border border-zinc-700 rounded-lg px-4 py-3 text-white focus:ring-2 focus:ring-blue-600 focus:border-transparent outline-none transition-all placeholder-gray-600"
                            required
                            min="0"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-400 mb-1">Tanggal Tagihan (1-31)</label>
                        <input
                            type="number"
                            min="1"
                            max="31"
                            value={formData.billingDate}
                            onChange={(e) => setFormData({ ...formData, billingDate: e.target.value })}
                            className="w-full bg-zinc-900 border border-zinc-700 rounded-lg px-4 py-3 text-white focus:ring-2 focus:ring-blue-600 focus:border-transparent outline-none transition-all placeholder-gray-600"
                            required
                        />
                    </div>
                </div>

                <div className="flex justify-end pt-4 space-x-3">
                    <button
                        type="button"
                        onClick={onCancel}
                        className="px-4 py-2 bg-gray-700 hover:bg-gray-600 text-white rounded-lg font-medium transition-colors"
                    >
                        Batal
                    </button>
                    <button
                        type="submit"
                        className="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium transition-colors flex items-center shadow-lg shadow-blue-900/20"
                    >
                        <Save className="w-4 h-4 mr-2" />
                        Simpan
                    </button>
                </div>
            </form>
        </div>
    );
}
