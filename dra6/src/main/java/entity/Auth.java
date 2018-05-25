package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Auth {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String login;
	private String pass;
	private Long token_id;
	
	public Auth() {
	}
	
	

	public Auth(String login, String pass, Long token_id) {
		this.login = login;
		this.pass = pass;
		this.token_id = token_id;
	}
	
	public Auth(String login, String pass) {
		this.login = login;
		this.pass = pass;
	}
	


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Long getToken_id() {
		return token_id;
	}

	public void setToken_id(Long token_id) {
		this.token_id = token_id;
	}



	@Override
	public String toString() {
		return "Auth [id=" + id + ", login=" + login + ", pass=" + pass + ", token_id=" + token_id + "]";
	}
	
	
	

}
