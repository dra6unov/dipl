package entity;

import javax.persistence.Entity;
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
	private Long user_id;
	
	@ManyToOne
	@JoinColumn(name="id", referencedColumnName="id", insertable=false, updatable=false)
	private User_inf user_inf;
	
	public User_file(){
		super();
	}

	public User_file(String file_name, Long user_id) {
		this.file_name = file_name;
		this.user_id = user_id;
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
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "User_file [id=" + id + ", file_name=" + file_name + ", user_id=" + user_id + "]";
	}
	
	

}
