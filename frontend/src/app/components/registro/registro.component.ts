import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { UserModel } from '../../models/user.model';

@Component({
  changeDetection: ChangeDetectionStrategy.Eager,
  selector: 'app-registro',
  standalone: false,
  styleUrl: './registro.component.css',
  templateUrl: './registro.component.html',
})
export class RegistroComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  usuario: UserModel = { username: '', password: '' };
  error: string = '';

  registrar(): void {
    this.error = '';
    this.authService.register(this.usuario).subscribe({
      next: () => this.router.navigate(['/login']),
      error: (err: HttpErrorResponse) => {
        this.error = typeof err.error === 'string' ? err.error : 'No fue posible registrarse';
      },
    });
  }
}
