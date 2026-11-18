import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { LacteoService } from '../../services/lacteo.service';
import { CarneService } from '../../services/carne.service';
import { ProductoModel } from '../../models/producto.model';

@Component({
  changeDetection: ChangeDetectionStrategy.Eager,
  selector: 'app-cliente',
  standalone: false,
  styleUrl: './cliente.component.css',
  templateUrl: './cliente.component.html',
})
export class ClienteComponent implements OnInit {
  private lacteoService = inject(LacteoService);
  private carneService = inject(CarneService);

  lacteos: ProductoModel[] = [];
  carnes: ProductoModel[] = [];
  cantidades: Record<string, number> = {};
  mensaje: string = '';
  esError: boolean = false;

  ngOnInit(): void {
    this.loadProductos();
  }

  loadProductos(): void {
    this.lacteoService.getAll().subscribe({
      next: (response) => (this.lacteos = response ?? []),
      error: (err: HttpErrorResponse) => this.mostrarError(err),
    });
    this.carneService.getAll().subscribe({
      next: (response) => (this.carnes = response ?? []),
      error: (err: HttpErrorResponse) => this.mostrarError(err),
    });
  }

  clave(tipo: string, producto: ProductoModel): string {
    return tipo + '-' + producto.id;
  }

  comprar(tipo: 'lacteo' | 'carne', producto: ProductoModel): void {
    const clave = this.clave(tipo, producto);
    const cantidad = this.cantidades[clave] ?? 1;
    if (producto.id === undefined || cantidad < 1) {
      this.esError = true;
      this.mensaje = 'La cantidad debe ser mayor a cero';
      return;
    }
    const servicio = tipo === 'lacteo' ? this.lacteoService : this.carneService;
    servicio.comprar(producto.id, cantidad).subscribe({
      next: (respuesta) => {
        this.esError = false;
        this.mensaje = respuesta;
        this.cantidades[clave] = 1;
        this.loadProductos();
      },
      error: (err: HttpErrorResponse) => this.mostrarError(err),
    });
  }

  private mostrarError(err: HttpErrorResponse): void {
    this.esError = true;
    this.mensaje = typeof err.error === 'string' && err.error ? err.error : 'Error en la operación';
  }
}
