package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Tokens {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String google_token;
	private String dropbox_token;
	
	public Tokens() {
		
	}
	
	

	public Tokens(String google_token, String dropbox_token) {
		this.google_token = google_token;
		this.dropbox_token = dropbox_token;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return "Tokens [id=" + id + ", google_token=" + google_token + ", dropbox_token=" + dropbox_token + "]";
	}
	
	

}
