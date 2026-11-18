package co.edu.unbosque.parcialfinal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unbosque.parcialfinal.model.Lacteo;

public interface LacteoRepository extends JpaRepository<Lacteo, Long> {

	public Optional<Lacteo> findByNombre(String nombre);

}
