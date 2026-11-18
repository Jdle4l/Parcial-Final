package co.edu.unbosque.parcialfinal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.parcialfinal.dto.CarneDTO;
import co.edu.unbosque.parcialfinal.model.Carne;
import co.edu.unbosque.parcialfinal.repository.CarneRepository;
import co.edu.unbosque.parcialfinal.util.ProductoValidator;

@Service
public class CarneService implements CRUDOperation<CarneDTO> {

	@Autowired
	private CarneRepository carneRepo;
	@Autowired
	private ModelMapper modelMapper;

	public CarneService() {
	}

	@Override
	public int create(CarneDTO data) {
		if (!ProductoValidator.isValid(data)) {
			return 2;
		}
		if (carneRepo.findByNombre(data.getNombre().trim()).isPresent()) {
			return 1;
		}
		Carne entity = modelMapper.map(data, Carne.class);
		entity.setId(null);
		entity.setNombre(data.getNombre().trim());
		carneRepo.save(entity);
		return 0;
	}

	@Override
	public List<CarneDTO> getAll() {
		List<Carne> entityList = carneRepo.findAll();
		List<CarneDTO> dtoList = new ArrayList<>();
		entityList.forEach(entity -> dtoList.add(modelMapper.map(entity, CarneDTO.class)));
		return dtoList;
	}

	public CarneDTO getById(Long id) {
		Optional<Carne> found = carneRepo.findById(id);
		if (found.isPresent()) {
			return modelMapper.map(found.get(), CarneDTO.class);
		} else {
			return null;
		}
	}

	@Override
	public int deleteById(Long id) {
		Optional<Carne> found = carneRepo.findById(id);
		if (found.isPresent()) {
			carneRepo.delete(found.get());
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int updateById(Long id, CarneDTO newData) {
		Optional<Carne> found = carneRepo.findById(id);
		if (!found.isPresent()) {
			return 2;
		}
		if (!ProductoValidator.isValid(newData)) {
			return 3;
		}
		Optional<Carne> sameName = carneRepo.findByNombre(newData.getNombre().trim());
		if (sameName.isPresent() && !sameName.get().getId().equals(id)) {
			return 1;
		}
		Carne temp = found.get();
		temp.setNombre(newData.getNombre().trim());
		temp.setCantidad(newData.getCantidad());
		temp.setPrecio(newData.getPrecio());
		temp.setUrlImagen(newData.getUrlImagen());
		carneRepo.save(temp);
		return 0;
	}

	@Transactional
	public int comprar(Long id, int cantidad) {
		if (cantidad <= 0) {
			return 3;
		}
		Optional<Carne> found = carneRepo.findById(id);
		if (!found.isPresent()) {
			return 1;
		}
		Carne temp = found.get();
		if (temp.getCantidad() < cantidad) {
			return 2;
		}
		temp.setCantidad(temp.getCantidad() - cantidad);
		carneRepo.save(temp);
		return 0;
	}

	@Override
	public long count() {
		return carneRepo.count();
	}

	@Override
	public boolean exist(Long id) {
		return carneRepo.existsById(id);
	}

}
