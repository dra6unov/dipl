package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User_inf {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String first_name;
	private String last_name;
	private Long auth_id;
	private Long group_id;
	private String email;
	
	public User_inf() {
	}

	public User_inf(String first_name, String last_name, Long auth_id, Long group_id, String email) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.auth_id = auth_id;
		this.group_id = group_id;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public Long getAuth_id() {
		return auth_id;
	}

	public void setAuth_id(Long auth_id) {
		this.auth_id = auth_id;
	}

	public Long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User_inf [id=" + id + ", first_name=" + first_name + ", last_name=" + last_name + ", auth_id=" + auth_id
				+ ", group_id=" + group_id + ", email=" + email + "]";
	}
	
	
	
	

}
