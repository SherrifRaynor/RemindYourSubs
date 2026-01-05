export function calculateDaysUntilBilling(billingDate) {
    const today = new Date();
    const currentDay = today.getDate();
    const currentMonth = today.getMonth();
    const currentYear = today.getFullYear();

    // Tanggal tagihan bulan ini
    let nextBilling = new Date(currentYear, currentMonth, billingDate);

    // Jika tanggal tagihan sudah lewat atau hari ini, cek untuk logika bulan depan?
    // User logic: 
    // Jika selisih <= 0: "JATUH TEMPO HARI INI" (artinya hari ini == billingDate)
    // Tapi jika hari ini > billingDate, maka targetnya adalah bulan depan.

    if (currentDay > billingDate) {
        nextBilling = new Date(currentYear, currentMonth + 1, billingDate);
    }

    // Hitung selisih waktu
    // Set jam ke 00:00:00 untuk akurasi hari
    today.setHours(0, 0, 0, 0);
    nextBilling.setHours(0, 0, 0, 0);

    const diffTime = nextBilling.getTime() - today.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    return diffDays;
}

export function getStatusColor(daysLeft) {
    if (daysLeft === 0) return 'bg-red-600 text-white'; // Today
    if (daysLeft < 0) return 'bg-red-800 text-white'; // Overdue (should handle logic if needed)
    if (daysLeft <= 3) return 'bg-orange-500 text-white'; // Urgent
    return 'bg-green-600 text-white'; // Safe
}

export function getStatusLabel(daysLeft) {
    if (daysLeft === 0) return "JATUH TEMPO HARI INI";
    if (daysLeft === 1) return "BESOK";
    if (daysLeft < 0) return "TERLEWAT"; // Should technically jump to next month if we use the logic above properly
    if (daysLeft <= 3) return `${daysLeft} HARI LAGI`;
    return "LUNAS BULAN INI"; // Or "X Hari Lagi" if long duration? User said "LUNAS BULAN INI" for > 3
}
