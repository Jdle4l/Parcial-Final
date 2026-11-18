package co.edu.unbosque.parcialfinal.controller;

import java.security.Principal;
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

import co.edu.unbosque.parcialfinal.dto.UserDTO;
import co.edu.unbosque.parcialfinal.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@Transactional
@Tag(name = "Usuarios", description = "Gestion de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

	@Autowired
	private UserService userServ;

	public UserController() {
	}

	@PostMapping(path = "/createjson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createNewWithJSON(@RequestBody UserDTO newUser) {
		int status = userServ.create(newUser);
		if (status == 0) {
			return new ResponseEntity<>("Usuario creado exitosamente", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("El nombre de usuario ya esta en uso", HttpStatus.NOT_ACCEPTABLE);
		} else {
			return new ResponseEntity<>("Datos invalidos", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getall")
	public ResponseEntity<List<UserDTO>> getAll() {
		List<UserDTO> users = userServ.getAll();
		if (users.isEmpty()) {
			return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
		}
	}

	@GetMapping("/count")
	public ResponseEntity<Long> countAll() {
		return new ResponseEntity<>(userServ.count(), HttpStatus.ACCEPTED);
	}

	@GetMapping("/exists/{id}")
	public ResponseEntity<Boolean> exists(@PathVariable Long id) {
		boolean found = userServ.exist(id);
		if (found) {
			return new ResponseEntity<>(true, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/getbyid/{id}")
	public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
		UserDTO found = userServ.getById(id);
		if (found != null) {
			return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(new UserDTO(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/me")
	public ResponseEntity<UserDTO> me(Principal principal) {
		UserDTO found = userServ.getByUsername(principal.getName());
		if (found != null) {
			return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>(new UserDTO(), HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(path = "/updatejson", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateWithJSON(@RequestParam Long id, @RequestBody UserDTO newUser) {
		int status = userServ.updateById(id, newUser);
		if (status == 0) {
			return new ResponseEntity<>("Usuario actualizado exitosamente", HttpStatus.ACCEPTED);
		} else if (status == 1) {
			return new ResponseEntity<>("El nuevo nombre de usuario ya esta en uso", HttpStatus.IM_USED);
		} else if (status == 2) {
			return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>("Datos invalidos", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/deletebyid/{id}")
	public ResponseEntity<String> deleteById(@PathVariable Long id) {
		int status = userServ.deleteById(id);
		if (status == 0) {
			return new ResponseEntity<>("Usuario eliminado exitosamente", HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/deletebyname")
	public ResponseEntity<String> deleteByName(@RequestParam String name) {
		int status = userServ.deleteByUsername(name);
		if (status == 0) {
			return new ResponseEntity<>("Usuario eliminado exitosamente", HttpStatus.ACCEPTED);
		} else {
			return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
		}
	}

}
