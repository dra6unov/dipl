package repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import entity.Dropbox_file;

@Repository
public interface Dropbox_fileRepo extends CrudRepository<Dropbox_file, Long>{

}
