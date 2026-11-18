import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { AuthResponseModel } from '../models/auth-response.model';
import { UserModel } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private cliente = inject(HttpClient);
  private router = inject(Router);
  private readonly urlbase: string = 'http://localhost:8080';

  login(username: string, password: string) {
    return this.cliente
      .post<AuthResponseModel>(this.urlbase + '/auth/login', { username, password })
      .pipe(
        tap((response) => {
          localStorage.setItem('token', response.token);
          localStorage.setItem('username', response.username);
          localStorage.setItem('role', response.role);
        }),
      );
  }

  register(usuario: UserModel) {
    return this.cliente.post(this.urlbase + '/auth/register', usuario, { responseType: 'text' });
  }

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  hasRole(...roles: string[]): boolean {
    const rol = this.getRole();
    return rol !== null && roles.includes(rol);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }
    try {
      const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
      return payload.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }
}
