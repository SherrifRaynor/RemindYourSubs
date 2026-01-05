import React from 'react';
import { getStatusColor, getStatusLabel } from '../utils/dateHelpers';

export default function StatusBadge({ daysLeft }) {
    const colorClass = getStatusColor(daysLeft);
    const label = getStatusLabel(daysLeft);

    return (
        <span className={`px-2 py-1 rounded-md text-xs font-bold uppercase tracking-wider ${colorClass}`}>
            {label}
        </span>
    );
}
