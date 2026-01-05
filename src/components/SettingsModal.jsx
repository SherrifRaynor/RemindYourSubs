import React, { useState, useEffect } from 'react';
import { X, Save, Mail, AlertCircle, CheckCircle, Loader2 } from 'lucide-react';
import { sendReminder } from '../services/EmailService';

export default function SettingsModal({ isOpen, onClose, config, onSave }) {
    const [formData, setFormData] = useState({
        userEmail: '',
        reminderDaysBefore: 3,
    });
    const [testStatus, setTestStatus] = useState({ type: '', message: '' });
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        if (isOpen) {
            setFormData({
                userEmail: config.userEmail || '',
                reminderDaysBefore: config.reminderDaysBefore || 3,
            });
            setTestStatus({ type: '', message: '' });
        }
    }, [isOpen, config]);

    const handleSave = (e) => {
        e.preventDefault();
        onSave(formData);
        onClose();
    };

    const handleTestEmail = async () => {
        // Check API key from props (Config) since it's not in form anymore
        if (!config.resendApiKey || !formData.userEmail) {
            setTestStatus({ type: 'error', message: 'Email konfigurasi tidak lengkap atau API Key belum diset di .env!' });
            return;
        }

        setIsLoading(true);
        setTestStatus({ type: 'info', message: 'Mengirim email test...' });

        try {
            const dummySub = {
                name: 'TEST SUBSCRIPTION',
                price: 100000,
                billingDate: new Date().getDate()
            };

            await sendReminder(dummySub, formData.userEmail, config.resendApiKey);
            setTestStatus({ type: 'success', message: 'Email berhasil dikirim! Cek inbox Anda.' });
        } catch (error) {
            setTestStatus({ type: 'error', message: `Gagal: ${error.message}` });
        } finally {
            setIsLoading(false);
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-sm animate-fade-in">
            <div className="bg-zinc-900 rounded-xl shadow-2xl border border-zinc-700 w-full max-w-md overflow-hidden transform transition-all scale-100">
                <div className="flex justify-between items-center p-6 border-b border-zinc-800">
                    <h2 className="text-xl font-bold text-white flex items-center">
                        <Mail className="w-5 h-5 mr-2 text-blue-500" />
                        Email Reminder Settings
                    </h2>
                    <button onClick={onClose} className="text-gray-400 hover:text-white transition-colors">
                        <X className="w-6 h-6" />
                    </button>
                </div>

                <form onSubmit={handleSave} className="p-6 space-y-4">
                    <div className="bg-blue-900/20 border border-blue-900/50 rounded-lg p-3 text-sm text-blue-200 mb-4">
                        <p className="flex items-start">
                            <AlertCircle className="w-4 h-4 mr-2 mt-0.5 flex-shrink-0" />
                            API Key dikonfigurasi melalui file <code>.env</code>
                        </p>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-400 mb-1">Email Penerima</label>
                        <input
                            type="email"
                            placeholder="email@anda.com"
                            value={formData.userEmail}
                            onChange={(e) => setFormData({ ...formData, userEmail: e.target.value })}
                            className="w-full bg-zinc-800 border border-zinc-700 rounded-lg px-4 py-2 text-white focus:ring-2 focus:ring-blue-600 outline-none placeholder-gray-600"
                            required
                        />
                    </div>


                    <div>
                        <label className="block text-sm font-medium text-gray-400 mb-1">Ingatkan X Hari Sebelumnya</label>
                        <input
                            type="number"
                            min="1"
                            max="7"
                            value={formData.reminderDaysBefore}
                            onChange={(e) => setFormData({ ...formData, reminderDaysBefore: e.target.value })}
                            className="w-full bg-zinc-800 border border-zinc-700 rounded-lg px-4 py-2 text-white focus:ring-2 focus:ring-blue-600 outline-none"
                        />
                    </div>

                    {testStatus.message && (
                        <div className={`p-3 rounded-lg text-sm flex items-center ${testStatus.type === 'error' ? 'bg-red-900/30 text-red-400' :
                            testStatus.type === 'success' ? 'bg-green-900/30 text-green-400' :
                                'bg-blue-900/30 text-blue-400'
                            }`}>
                            {testStatus.type === 'success' ? <CheckCircle className="w-4 h-4 mr-2" /> :
                                testStatus.type === 'error' ? <AlertCircle className="w-4 h-4 mr-2" /> :
                                    null}
                            {testStatus.message}
                        </div>
                    )}

                    <div className="flex justify-between items-center pt-4 border-t border-zinc-800 mt-4">
                        <button
                            type="button"
                            onClick={handleTestEmail}
                            disabled={isLoading}
                            className="text-sm text-gray-400 hover:text-white underline decoration-dotted disabled:opacity-50"
                        >
                            {isLoading ? <Loader2 className="w-4 h-4 animate-spin inline mr-1" /> : 'Test Email Connection'}
                        </button>
                        <button
                            type="submit"
                            className="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium transition-colors shadow-lg shadow-blue-900/20"
                        >
                            Simpan
                        </button>
                    </div>
                </form>
            </div >
        </div >
    );
}
