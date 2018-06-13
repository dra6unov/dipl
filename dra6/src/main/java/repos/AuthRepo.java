package repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import entity.Auth;

@Repository
public interface AuthRepo extends CrudRepository<Auth, Long>{
	List<Auth> findByLogin(String login);
	
	List<Auth> findByLoginAndPass(String login, String pass);
	
	//Integer findByLogin1(String login);
	
	//List<Auth> findByToken_id (String token_id);

}
