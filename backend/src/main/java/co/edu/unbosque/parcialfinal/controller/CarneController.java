package co.edu.unbosque.parcialfinal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.parcialfinal.dto.CarneDTO;
import co.edu.unbosque.parcialfinal.service.CarneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/carne")
@Transactional
@Tag(name = "Carnes", description = "Gestion y compra de productos carnes")
@SecurityRequirement(name = "bearerAuth")
public class CarneController {

	@Autowired
	private CarneService carneServ;

	public CarneController() {
	}

	@Operation(summary = "Crear carne (ADMIN)")
	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createNewWithJSON(@RequestBody CarneDTO newCarne) {
		int status = carneServ.create(newCarne);
		if (status == 0) {
			return new ResponseEntity<>("Carne creada exitosamente", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("Ya existe una carne con ese nombre", HttpStatus.NOT_ACCEPTABLE);
		} else {
			return new ResponseEntity<>("Datos invalidos", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Listar carnes (USER, ADMIN)")
	@GetMapping("/getall")
	public ResponseEntity<List<CarneDTO>> getAll() {
		List<CarneDTO> carnes = carneServ.getAll();
		if (carnes.isEmpty()) {
			return new ResponseEntity<>(carnes, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(carnes, HttpStatus.ACCEPTED);
		}
	}

	@Operation(summary = "Buscar carne por id (USER, ADMIN)")
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<CarneDTO> getById(@PathVariable Long id) {
		CarneDTO found = carneServ.getById(id);
		if (found != null) {
			return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(new CarneDTO(), HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Actualizar carne (ADMIN)")
	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateWithJSON(@RequestParam Long id, @RequestBody CarneDTO newCarne) {
		int status = carneServ.updateById(id, newCarne);
		if (status == 0) {
			return new ResponseEntity<>("Carne actualizada exitosamente", HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<>("Ya existe una carne con ese nombre", HttpStatus.IM_USED);
		} else if (status == 2) {
			return new ResponseEntity<>("Carne no encontrada", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>("Datos invalidos", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Eliminar carne (ADMIN)")
	@DeleteMapping("/deletebyid/{id}")
	public ResponseEntity<String> deleteById(@PathVariable Long id) {
		int status = carneServ.deleteById(id);
		if (status == 0) {
			return new ResponseEntity<>("Carne eliminada exitosamente", HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>("Carne no encontrada", HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Comprar carne (USER)", description = "Resta la cantidad comprada de las existencias")
	@PutMapping("/comprar")
	public ResponseEntity<String> comprar(@RequestParam Long id, @RequestParam int cantidad) {
		CarneDTO found = carneServ.getById(id);
		int status = carneServ.comprar(id, cantidad);
		if (status == 0) {
			return new ResponseEntity<>("Compra exitosa de " + cantidad + " " + found.getNombre() + ". Total: $"
					+ String.format("%,.0f", found.getPrecio() * cantidad), HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<>("Carne no encontrada", HttpStatus.NOT_FOUND);
		} else if (status == 2) {
			return new ResponseEntity<>("No hay suficientes existencias", HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>("La cantidad debe ser mayor a cero", HttpStatus.BAD_REQUEST);
		}
	}

}
