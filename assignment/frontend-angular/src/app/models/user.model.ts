export interface UserConfig {
    id?: number;
    email: string;
    name: string;
    reminderDaysBefore: number;
    resendApiKey?: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface UserRequest {
    email: string;
    name: string;
    reminderDaysBefore: number;
    resendApiKey?: string;
}
