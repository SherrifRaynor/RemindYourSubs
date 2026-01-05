import React, { useState, useEffect } from 'react';
import { Plus, Settings, CreditCard, Wallet } from 'lucide-react';
import SubscriptionCard from './components/SubscriptionCard';
import SubscriptionForm from './components/SubscriptionForm';
import TotalExpense from './components/TotalExpense';
import SettingsModal from './components/SettingsModal';
import { generateId } from './utils/uuid';
import { checkAndSendReminders } from './services/ReminderChecker';

function App() {
  // --- State Management ---
  const [subscriptions, setSubscriptions] = useState(() => {
    const saved = localStorage.getItem('remindyoursubs_subscriptions');
    return saved ? JSON.parse(saved) : [];
  });

  const [config, setConfig] = useState(() => {
    const saved = localStorage.getItem('remindyoursubs_config');
    const envApiKey = import.meta.env.VITE_RESEND_API_KEY;

    // Always prefer env var if available for security, or fallback to saved (though we are removing UI)
    const initialConfig = saved ? JSON.parse(saved) : {
      userEmail: '',
      reminderDaysBefore: 3
    };

    // Force overwrite API Key with Env Var if it exists
    if (envApiKey) {
      initialConfig.resendApiKey = envApiKey;
    }

    return initialConfig;
  });

  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);
  const [editingSub, setEditingSub] = useState(null);
  const [toast, setToast] = useState({ show: false, message: '', type: '' });

  // --- Effects ---

  // Persist Subscriptions
  useEffect(() => {
    localStorage.setItem('remindyoursubs_subscriptions', JSON.stringify(subscriptions));
  }, [subscriptions]);

  // Persist Config
  useEffect(() => {
    localStorage.setItem('remindyoursubs_config', JSON.stringify(config));
  }, [config]);

  // Check Reminders on Mount & Config Change
  useEffect(() => {
    const runCheck = async () => {
      await checkAndSendReminders(subscriptions, config, (updatedSubs) => {
        // Merge updates
        setSubscriptions(prev => {
          return prev.map(sub => {
            const update = updatedSubs.find(u => u.id === sub.id);
            return update ? update : sub;
          });
        });
        showToast(`Email reminder sent for ${updatedSubs.length} subscriptions!`, 'success');
      });
    };

    runCheck();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [config.userEmail, config.reminderDaysBefore]); // API Key is now static from env usually

  // --- Handlers ---

  const showToast = (message, type = 'success') => {
    setToast({ show: true, message, type });
    setTimeout(() => setToast({ show: false, message: '', type: '' }), 3000);
  };

  const handleSaveSubscription = (data) => {
    if (editingSub) {
      setSubscriptions(subscriptions.map(sub =>
        sub.id === editingSub.id ? { ...sub, ...data } : sub
      ));
      showToast('Langganan berhasil diperbarui');
    } else {
      setSubscriptions([...subscriptions, {
        id: generateId(),
        ...data,
        isActive: true,
        reminderEnabled: true,
        lastReminderSent: null
      }]);
      showToast('Langganan baru ditambahkan');
    }
    setIsFormOpen(false);
    setEditingSub(null);
  };

  const handleDelete = (id) => {
    if (window.confirm('Yakin ingin menghapus langganan ini?')) {
      setSubscriptions(subscriptions.filter(sub => sub.id !== id));
      showToast('Langganan dihapus', 'info');
    }
  };

  const handleEdit = (sub) => {
    setEditingSub(sub);
    setIsFormOpen(true);
  };

  const handleToggleReminder = (id) => {
    setSubscriptions(subscriptions.map(sub =>
      sub.id === id ? { ...sub, reminderEnabled: !sub.reminderEnabled } : sub
    ));
  };

  const handleSaveConfig = (newConfig) => {
    setConfig(newConfig);
    showToast('Pengaturan disimpan');
  };

  return (
    <div className="min-h-screen bg-zinc-900 text-white font-sans selection:bg-blue-500 selection:text-white">
      {/* Toast Notification */}
      {toast.show && (
        <div className={`fixed top-4 right-4 z-50 px-4 py-2 rounded-lg shadow-lg transform transition-all animate-bounce-in ${toast.type === 'error' ? 'bg-red-600' : 'bg-green-600'
          }`}>
          {toast.message}
        </div>
      )}

      {/* Navbar */}
      <nav className="border-b border-zinc-800 bg-zinc-900/50 backdrop-blur-md sticky top-0 z-40">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <div className="bg-blue-600 p-2 rounded-lg">
              <CreditCard className="w-5 h-5 text-white" />
            </div>
            <h1 className="text-xl font-bold tracking-tight">RemindYourSubs</h1>
          </div>
          <button
            onClick={() => setIsSettingsOpen(true)}
            className="p-2 hover:bg-zinc-800 rounded-lg text-gray-400 hover:text-white transition-colors"
          >
            <Settings className="w-5 h-5" />
          </button>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

        {/* Header Section */}
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
          <div>
            <h1 className="text-3xl font-bold text-white mb-2">Dashboard Langganan</h1>
            <p className="text-gray-400">Kelola semua tagihan bulananmu di satu tempat.</p>
          </div>

          {!isFormOpen && (
            <button
              onClick={() => { setEditingSub(null); setIsFormOpen(true); }}
              className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium transition-all shadow-lg shadow-blue-900/20 flex items-center"
            >
              <Plus className="w-5 h-5 mr-2" />
              Tambah Langganan
            </button>
          )}
        </div>

        {/* Stats Section */}
        <TotalExpense subscriptions={subscriptions} />

        {/* Form Section */}
        {isFormOpen && (
          <SubscriptionForm
            initialData={editingSub}
            onSave={handleSaveSubscription}
            onCancel={() => { setIsFormOpen(false); setEditingSub(null); }}
          />
        )}

        {/* Subscriptions Grid */}
        {subscriptions.length === 0 && !isFormOpen ? (
          <div className="text-center py-20 bg-zinc-800/50 rounded-lg border border-zinc-800 border-dashed">
            <div className="bg-zinc-800 inline-flex p-4 rounded-full mb-4">
              <Wallet className="w-8 h-8 text-gray-500" />
            </div>
            <h3 className="text-xl font-semibold text-white mb-2">Belum ada langganan</h3>
            <p className="text-gray-400 mb-6">Mulai catat pengeluaran bulananmu sekarang.</p>
            <button
              onClick={() => setIsFormOpen(true)}
              className="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium transition-colors"
            >
              Tambah Pertama
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {subscriptions.map(sub => (
              <SubscriptionCard
                key={sub.id}
                subscription={sub}
                onEdit={handleEdit}
                onDelete={handleDelete}
                onToggleReminder={handleToggleReminder}
              />
            ))}
          </div>
        )}
      </main>

      {/* Modals */}
      <SettingsModal
        isOpen={isSettingsOpen}
        onClose={() => setIsSettingsOpen(false)}
        config={config}
        onSave={handleSaveConfig}
      />
    </div>
  );
}

export default App;
