import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserConfig, UserRequest } from '../models/user.model';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private apiUrl = `${environment.apiUrl}/users`;

    constructor(private http: HttpClient) { }

    getAllUsers(): Observable<UserConfig[]> {
        return this.http.get<UserConfig[]>(this.apiUrl);
    }

    getUserById(id: number): Observable<UserConfig> {
        return this.http.get<UserConfig>(`${this.apiUrl}/${id}`);
    }

    getUserByEmail(email: string): Observable<UserConfig> {
        return this.http.get<UserConfig>(`${this.apiUrl}/email/${email}`);
    }

    createUser(user: UserRequest): Observable<UserConfig> {
        return this.http.post<UserConfig>(this.apiUrl, user);
    }

    updateUser(id: number, user: UserRequest): Observable<UserConfig> {
        return this.http.put<UserConfig>(`${this.apiUrl}/${id}`, user);
    }

    deleteUser(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
