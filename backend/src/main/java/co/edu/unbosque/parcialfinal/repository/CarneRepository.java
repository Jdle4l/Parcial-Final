package co.edu.unbosque.parcialfinal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.parcialfinal.model.Carne;

public interface CarneRepository extends JpaRepository<Carne, Long> {

	public Optional<Carne> findByNombre(String nombre);

}
