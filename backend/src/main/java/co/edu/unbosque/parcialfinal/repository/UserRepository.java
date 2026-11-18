package co.edu.unbosque.parcialfinal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.parcialfinal.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByUsername(String username);

	public boolean existsByUsername(String username);

	public void deleteByUsername(String username);

}
