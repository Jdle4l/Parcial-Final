package co.edu.unbosque.parcialfinal.util;

import co.edu.unbosque.parcialfinal.dto.ProductoDTO;

public class ProductoValidator {

	private ProductoValidator() {
	}

	public static boolean isValid(ProductoDTO data) {
		return data != null && isValidText(data.getNombre()) && data.getCantidad() >= 0 && data.getPrecio() > 0
				&& isValidUrl(data.getUrlImagen());
	}

	private static boolean isValidText(String text) {
		return text != null && !text.isBlank() && !text.contains("<") && !text.contains(">");
	}

	private static boolean isValidUrl(String url) {
		return isValidText(url) && (url.startsWith("http://") || url.startsWith("https://"));
	}

}
