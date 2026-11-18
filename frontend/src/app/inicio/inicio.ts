import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  changeDetection: ChangeDetectionStrategy.Eager,
  selector: 'app-inicio',
  standalone: false,
  styleUrl: './inicio.css',
  templateUrl: './inicio.html',
})
export class Inicio {
  authService = inject(AuthService);
}
