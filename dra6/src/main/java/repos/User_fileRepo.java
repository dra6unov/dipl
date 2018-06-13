package repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import entity.User_file;


public interface User_fileRepo extends CrudRepository<User_file, Long>{
	//List<User_file> findByFilename(String file_name);

}
