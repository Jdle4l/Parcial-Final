import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './login/login';
import { Registro } from './registro/registro';
import { Inicio } from './inicio/inicio';
import { Admin } from './admin/admin';
import { Cliente } from './cliente/cliente';
import { authGuard, guestGuard } from './guards/auth-guard';

const routes: Routes = [
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },
  { path: 'login', component: Login, canActivate: [guestGuard] },
  { path: 'registro', component: Registro, canActivate: [guestGuard] },
  { path: 'inicio', component: Inicio, canActivate: [authGuard] },
  { path: 'admin', component: Admin, canActivate: [authGuard], data: { roles: ['ADMIN'] } },
  { path: 'cliente', component: Cliente, canActivate: [authGuard], data: { roles: ['USER'] } },
  { path: '**', redirectTo: 'inicio' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
