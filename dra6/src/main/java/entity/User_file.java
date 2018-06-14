package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class User_file {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String file_name;
	@Column(name="user_id")
	private Long file;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id", insertable=false, updatable=false)
	private User_inf user;
	
	public User_file(){
		super();
	}

	public User_file(String file_name, Long userid) {
		this.file_name = file_name;
		this.file = userid;
	}

	

	public User_inf getUser_inf() {
		return user;
	}

	public void setUser_inf(User_inf user_inf) {
		this.user = user_inf;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public Long getUser_id() {
		return file;
	}

	public void setUser_id(Long user_id) {
		this.file = user_id;
	}

	@Override
	public String toString() {
		return "User_file [id=" + id + ", file_name=" + file_name + ", user_id=" + file + "]";
	}
	
	

}
