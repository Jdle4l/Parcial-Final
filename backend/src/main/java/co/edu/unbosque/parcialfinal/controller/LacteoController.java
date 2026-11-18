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

import co.edu.unbosque.parcialfinal.dto.LacteoDTO;
import co.edu.unbosque.parcialfinal.service.LacteoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/lacteo")
@Transactional
@Tag(name = "Lacteos", description = "Gestion y compra de productos lacteos")
@SecurityRequirement(name = "bearerAuth")
public class LacteoController {

	@Autowired
	private LacteoService lacteoServ;

	public LacteoController() {
	}

	@Operation(summary = "Crear lacteo (ADMIN)")
	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createNewWithJSON(@RequestBody LacteoDTO newLacteo) {
		int status = lacteoServ.create(newLacteo);
		if (status == 0) {
			return new ResponseEntity<>("Lacteo creado exitosamente", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("Ya existe un lacteo con ese nombre", HttpStatus.NOT_ACCEPTABLE);
		} else {
			return new ResponseEntity<>("Datos invalidos", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Listar lacteos (USER, ADMIN)")
	@GetMapping("/getall")
	public ResponseEntity<List<LacteoDTO>> getAll() {
		List<LacteoDTO> lacteos = lacteoServ.getAll();
		if (lacteos.isEmpty()) {
			return new ResponseEntity<>(lacteos, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(lacteos, HttpStatus.ACCEPTED);
		}
	}

	@Operation(summary = "Buscar lacteo por id (USER, ADMIN)")
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<LacteoDTO> getById(@PathVariable Long id) {
		LacteoDTO found = lacteoServ.getById(id);
		if (found != null) {
			return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(new LacteoDTO(), HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Actualizar lacteo (ADMIN)")
	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateWithJSON(@RequestParam Long id, @RequestBody LacteoDTO newLacteo) {
		int status = lacteoServ.updateById(id, newLacteo);
		if (status == 0) {
			return new ResponseEntity<>("Lacteo actualizado exitosamente", HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<>("Ya existe un lacteo con ese nombre", HttpStatus.IM_USED);
		} else if (status == 2) {
			return new ResponseEntity<>("Lacteo no encontrado", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>("Datos invalidos", HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Eliminar lacteo (ADMIN)")
	@DeleteMapping("/deletebyid/{id}")
	public ResponseEntity<String> deleteById(@PathVariable Long id) {
		int status = lacteoServ.deleteById(id);
		if (status == 0) {
			return new ResponseEntity<>("Lacteo eliminado exitosamente", HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>("Lacteo no encontrado", HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Comprar lacteo (USER)", description = "Resta la cantidad comprada de las existencias")
	@PutMapping("/comprar")
	public ResponseEntity<String> comprar(@RequestParam Long id, @RequestParam int cantidad) {
		LacteoDTO found = lacteoServ.getById(id);
		int status = lacteoServ.comprar(id, cantidad);
		if (status == 0) {
			return new ResponseEntity<>("Compra exitosa de " + cantidad + " " + found.getNombre() + ". Total: $"
					+ String.format("%,.0f", found.getPrecio() * cantidad), HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<>("Lacteo no encontrado", HttpStatus.NOT_FOUND);
		} else if (status == 2) {
			return new ResponseEntity<>("No hay suficientes existencias", HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>("La cantidad debe ser mayor a cero", HttpStatus.BAD_REQUEST);
		}
	}

}
