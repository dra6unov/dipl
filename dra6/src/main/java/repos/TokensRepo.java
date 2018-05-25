package repos;

import org.springframework.data.repository.CrudRepository;

import entity.Tokens;

public interface TokensRepo extends CrudRepository<Tokens, Long>{

}
