package repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import entity.Google_file;

public interface Google_fileRepo extends CrudRepository<Google_file, Long>{
	List<Google_file> findByFile(String file);

}
