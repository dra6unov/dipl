package repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import entity.Auth;

public interface AuthRepo extends CrudRepository<Auth, Long>{
	List<Auth> findByLogin(String login);
	
	List<Auth> findByLoginAndPass(String login, String pass);

}
