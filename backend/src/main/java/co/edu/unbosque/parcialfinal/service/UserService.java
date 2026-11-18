package co.edu.unbosque.parcialfinal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.parcialfinal.dto.UserDTO;
import co.edu.unbosque.parcialfinal.model.User;
import co.edu.unbosque.parcialfinal.repository.UserRepository;

@Service
public class UserService implements CRUDOperation<UserDTO> {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserService() {
	}

	@Override
	public int create(UserDTO data) {
		if (!isValid(data.getUsername()) || !isValid(data.getPassword())) {
			return 2;
		}
		if (findUsernameAlreadyTaken(data.getUsername())) {
			return 1;
		}
		User entity = modelMapper.map(data, User.class);
		entity.setId(null);
		if (entity.getRole() == null) {
			entity.setRole(User.Role.USER);
		}
		entity.setPassword(passwordEncoder.encode(data.getPassword()));
		userRepo.save(entity);
		return 0;
	}

	public int register(UserDTO data) {
		data.setRole(User.Role.USER);
		return create(data);
	}

	@Override
	public List<UserDTO> getAll() {
		List<User> entityList = userRepo.findAll();
		List<UserDTO> dtoList = new ArrayList<>();
		entityList.forEach(entity -> dtoList.add(modelMapper.map(entity, UserDTO.class)));
		return dtoList;
	}

	@Override
	public int deleteById(Long id) {
		Optional<User> found = userRepo.findById(id);
		if (found.isPresent()) {
			userRepo.delete(found.get());
			return 0;
		} else {
			return 1;
		}
	}

	public int deleteByUsername(String username) {
		Optional<User> found = userRepo.findByUsername(username);
		if (found.isPresent()) {
			userRepo.delete(found.get());
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int updateById(Long id, UserDTO newData) {
		Optional<User> found = userRepo.findById(id);
		if (!found.isPresent()) {
			return 2;
		}
		if (!isValid(newData.getUsername())) {
			return 3;
		}
		Optional<User> sameName = userRepo.findByUsername(newData.getUsername());
		if (sameName.isPresent() && !sameName.get().getId().equals(id)) {
			return 1;
		}
		User temp = found.get();
		temp.setUsername(newData.getUsername());
		if (newData.getPassword() != null && !newData.getPassword().isBlank()) {
			temp.setPassword(passwordEncoder.encode(newData.getPassword()));
		}
		if (newData.getRole() != null) {
			temp.setRole(newData.getRole());
		}
		userRepo.save(temp);
		return 0;
	}

	@Override
	public long count() {
		return userRepo.count();
	}

	@Override
	public boolean exist(Long id) {
		return userRepo.existsById(id);
	}

	public UserDTO getById(Long id) {
		Optional<User> found = userRepo.findById(id);
		if (found.isPresent()) {
			return modelMapper.map(found.get(), UserDTO.class);
		} else {
			return null;
		}
	}

	public UserDTO getByUsername(String username) {
		Optional<User> found = userRepo.findByUsername(username);
		if (found.isPresent()) {
			return modelMapper.map(found.get(), UserDTO.class);
		} else {
			return null;
		}
	}

	public boolean findUsernameAlreadyTaken(String username) {
		return userRepo.existsByUsername(username);
	}

	private boolean isValid(String text) {
		return text != null && !text.isBlank() && !text.contains("<") && !text.contains(">");
	}

}
