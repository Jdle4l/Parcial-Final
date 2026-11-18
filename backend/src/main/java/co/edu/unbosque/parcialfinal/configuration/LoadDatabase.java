package co.edu.unbosque.parcialfinal.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.unbosque.parcialfinal.model.User;
import co.edu.unbosque.parcialfinal.repository.UserRepository;

@Configuration
public class LoadDatabase {

	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Value("${app.admin.username:admin}")
	private String adminUsername;

	@Value("${app.admin.password:1234567890}")
	private String adminPassword;

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepo, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepo.findByUsername(adminUsername).isPresent()) {
				log.info("El administrador ya existe, omitiendo creacion...");
			} else {
				userRepo.save(new User(adminUsername, passwordEncoder.encode(adminPassword), User.Role.ADMIN));
				log.info("Precargando usuario administrador");
			}
			if (userRepo.findByUsername("normaluser").isPresent()) {
				log.info("El usuario normal ya existe, omitiendo creacion...");
			} else {
				userRepo.save(new User("normaluser", passwordEncoder.encode("1234567890"), User.Role.USER));
				log.info("Precargando usuario normal");
			}
		};
	}

}
