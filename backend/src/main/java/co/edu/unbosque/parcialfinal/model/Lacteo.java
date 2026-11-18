package co.edu.unbosque.parcialfinal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "lacteo")
public class Lacteo extends Producto {

	public Lacteo() {
		super();
	}

	public Lacteo(String nombre, int cantidad, double precio, String urlImagen) {
		super(nombre, cantidad, precio, urlImagen);
	}

}
