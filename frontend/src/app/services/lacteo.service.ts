import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ProductoModel } from '../models/producto.model';

@Injectable({
  providedIn: 'root',
})
export class LacteoService {
  private cliente = inject(HttpClient);
  private readonly urlbase: string = 'http://localhost:8080/lacteo';

  getAll() {
    return this.cliente.get<ProductoModel[] | null>(this.urlbase + '/getall');
  }

  create(producto: ProductoModel) {
    return this.cliente.post(this.urlbase + '/createjson', producto, { responseType: 'text' });
  }

  update(id: number, producto: ProductoModel) {
    return this.cliente.put(this.urlbase + '/updatejson?id=' + id, producto, {
      responseType: 'text',
    });
  }

  deleteById(id: number) {
    return this.cliente.delete(this.urlbase + '/deletebyid/' + id, { responseType: 'text' });
  }

  comprar(id: number, cantidad: number) {
    return this.cliente.put(this.urlbase + '/comprar?id=' + id + '&cantidad=' + cantidad, null, {
      responseType: 'text',
    });
  }
}
