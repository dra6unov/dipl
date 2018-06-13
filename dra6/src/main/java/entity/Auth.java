package entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Auth {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String login;
	private String pass;
	private String google_token;
	private String dropbox_token;
	
	public Auth() {
		
	}
	
	public Auth(String login, String pass, String google_token, String dropbox_token) {
		this.login = login;
		this.pass = pass;
		this.google_token = google_token;
		this.dropbox_token = dropbox_token;
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

	public String getGoogle_token() {
		return google_token;
	}

	public void setGoogle_token(String google_token) {
		this.google_token = google_token;
	}

	public String getDropbox_token() {
		return dropbox_token;
	}

	public void setDropbox_token(String dropbox_token) {
		this.dropbox_token = dropbox_token;
	}

	@Override
	public String toString() {
		return "Auth [id=" + id + ", login=" + login + ", pass=" + pass + ", google_token=" + google_token
				+ ", dropbox_token=" + dropbox_token + "]";
	}
	
	
	
	
	
	

}
