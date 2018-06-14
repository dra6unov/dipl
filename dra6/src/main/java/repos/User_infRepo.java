package repos;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import entity.User_inf;

public interface User_infRepo extends CrudRepository<User_inf, Long>{
	Optional<User_inf> findByFiles(Long id);
}
