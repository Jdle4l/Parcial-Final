package co.edu.unbosque.parcialfinal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "carne")
public class Carne extends Producto {

	public Carne() {
		super();
	}

	public Carne(String nombre, int cantidad, double precio, String urlImagen) {
		super(nombre, cantidad, precio, urlImagen);
	}

}
