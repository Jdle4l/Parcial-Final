import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { LacteoService } from '../../services/lacteo.service';
import { CarneService } from '../../services/carne.service';
import { ProductoModel } from '../../models/producto.model';

@Component({
  changeDetection: ChangeDetectionStrategy.Eager,
  selector: 'app-admin',
  standalone: false,
  styleUrl: './admin.component.css',
  templateUrl: './admin.component.html',
})
export class AdminComponent implements OnInit {
  private lacteoService = inject(LacteoService);
  private carneService = inject(CarneService);

  tipo: 'lacteo' | 'carne' = 'lacteo';
  productos: ProductoModel[] = [];
  form: ProductoModel = this.nuevoForm();
  editandoId: number | null = null;
  mensaje: string = '';
  esError: boolean = false;

  ngOnInit(): void {
    this.loadProductos();
  }

  get titulo(): string {
    return this.tipo === 'lacteo' ? 'Lácteos' : 'Carnes';
  }

  private get servicio(): LacteoService | CarneService {
    return this.tipo === 'lacteo' ? this.lacteoService : this.carneService;
  }

  cambiarTipo(tipo: 'lacteo' | 'carne'): void {
    this.tipo = tipo;
    this.cancelar();
    this.mensaje = '';
    this.loadProductos();
  }

  loadProductos(): void {
    this.servicio.getAll().subscribe({
      next: (response) => (this.productos = response ?? []),
      error: (err: HttpErrorResponse) => {
        this.productos = [];
        this.mostrarError(err);
      },
    });
  }

  guardar(): void {
    const peticion =
      this.editandoId === null
        ? this.servicio.create(this.form)
        : this.servicio.update(this.editandoId, this.form);
    peticion.subscribe({
      next: (respuesta) => {
        this.mostrarExito(respuesta);
        this.cancelar();
        this.loadProductos();
      },
      error: (err: HttpErrorResponse) => this.mostrarError(err),
    });
  }

  editar(producto: ProductoModel): void {
    this.editandoId = producto.id ?? null;
    this.form = { ...producto };
  }

  cancelar(): void {
    this.editandoId = null;
    this.form = this.nuevoForm();
  }

  eliminar(producto: ProductoModel): void {
    if (producto.id === undefined || !confirm('¿Eliminar "' + producto.nombre + '"?')) {
      return;
    }
    this.servicio.deleteById(producto.id).subscribe({
      next: (respuesta) => {
        this.mostrarExito(respuesta);
        this.loadProductos();
      },
      error: (err: HttpErrorResponse) => this.mostrarError(err),
    });
  }

  private mostrarExito(texto: string): void {
    this.esError = false;
    this.mensaje = texto;
  }

  private mostrarError(err: HttpErrorResponse): void {
    this.esError = true;
    this.mensaje = typeof err.error === 'string' && err.error ? err.error : 'Error en la operación';
  }

  private nuevoForm(): ProductoModel {
    return { nombre: '', cantidad: 0, precio: 0, urlImagen: '' };
  }
}
