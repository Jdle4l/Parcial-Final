package co.edu.unbosque.parcialfinal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.parcialfinal.dto.AuthResponseDTO;
import co.edu.unbosque.parcialfinal.dto.UserDTO;
import co.edu.unbosque.parcialfinal.model.User;
import co.edu.unbosque.parcialfinal.security.JwtUtil;
import co.edu.unbosque.parcialfinal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacion", description = "Inicio de sesion y registro de usuarios")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserService userService;

	public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.userService = userService;
	}

	@Operation(summary = "Iniciar sesion", description = "Retorna el token JWT, el usuario y su rol")
	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody UserDTO loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			User user = (User) authentication.getPrincipal();
			String jwt = jwtUtil.generateToken(user);
			return ResponseEntity.ok(new AuthResponseDTO(jwt, user.getUsername(), user.getRole().name()));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
		}
	}

	@Operation(summary = "Registrar usuario", description = "Crea una cuenta nueva con rol USER")
	@PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> register(@RequestBody UserDTO registerRequest) {
		int status = userService.register(registerRequest);
		if (status == 0) {
			return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.CREATED);
		} else if (status == 1) {
			return new ResponseEntity<>("El nombre de usuario ya existe", HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>("Datos invalidos", HttpStatus.BAD_REQUEST);
		}
	}

}
