export async function sendReminder(subscription, userEmail, apiKey) {
    try {
        // Use proxy '/api/resend' configured in vite.config.js to bypass CORS
        const response = await fetch('/api/resend/emails', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${apiKey}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                from: 'Subscription Tracker <onboarding@resend.dev>',
                to: [userEmail],
                subject: `Reminder: ${subscription.name} jatuh tempo dalam 3 hari`,
                html: `
          <div style="font-family: sans-serif; color: #333;">
            <h2>Reminder Tagihan 🔔</h2>
            <p>Halo,</p>
            <p>Langganan <strong>${subscription.name}</strong> sebesar <strong>Rp ${parseInt(subscription.price).toLocaleString('id-ID')}</strong> akan jatuh tempo pada tanggal <strong>${subscription.billingDate}</strong>.</p>
            <p>Jangan lupa untuk melakukan pembayaran ya! 💳</p>
            <hr>
            <p style="font-size: 12px; color: #666;">Sent by RemindYourSubs app.</p>
          </div>
        `
            })
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to send email');
        }

        return await response.json();
    } catch (error) {
        console.error('EmailService Error:', error);
        throw error;
    }
}
