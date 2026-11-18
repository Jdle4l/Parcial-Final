import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();
  const esAuth = req.url.includes('/auth/');

  const request =
    token && !esAuth ? req.clone({ setHeaders: { Authorization: 'Bearer ' + token } }) : req;

  return next(request).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401 && !esAuth) {
        authService.logout();
      }
      return throwError(() => err);
    }),
  );
};
