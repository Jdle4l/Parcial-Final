import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../services/auth.service';

@Component({
  changeDetection: ChangeDetectionStrategy.Eager,
  selector: 'app-login',
  standalone: false,
  styleUrl: './login.css',
  templateUrl: './login.html',
})
export class Login {
  private authService = inject(AuthService);
  private router = inject(Router);

  username: string = '';
  password: string = '';
  error: string = '';

  login(): void {
    this.error = '';
    this.authService.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/inicio']),
      error: (err: HttpErrorResponse) => {
        this.error = typeof err.error === 'string' ? err.error : 'No fue posible iniciar sesión';
      },
    });
  }
}
