package co.edu.unbosque.parcialfinal.dto;

import java.util.Objects;

public abstract class ProductoDTO {

	private Long id;
	private String nombre;
	private int cantidad;
	private double precio;
	private String urlImagen;

	protected ProductoDTO() {
	}

	protected ProductoDTO(String nombre, int cantidad, double precio, String urlImagen) {
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.precio = precio;
		this.urlImagen = urlImagen;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cantidad, id, nombre, precio, urlImagen);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductoDTO other = (ProductoDTO) obj;
		return cantidad == other.cantidad && Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& Double.doubleToLongBits(precio) == Double.doubleToLongBits(other.precio)
				&& Objects.equals(urlImagen, other.urlImagen);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [id=" + id + ", nombre=" + nombre + ", cantidad=" + cantidad
				+ ", precio=" + precio + ", urlImagen=" + urlImagen + "]";
	}

}
