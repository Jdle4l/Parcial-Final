import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { AuthService } from './services/auth.service';

@Component({
  changeDetection: ChangeDetectionStrategy.Eager,
  selector: 'app-root',
  standalone: false,
  styleUrl: './app.component.css',
  templateUrl: './app.component.html',
})
export class AppComponent {
  authService = inject(AuthService);
}
