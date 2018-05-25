package entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Google_file {
	@Id
	private Long id;
	private String file_id;
	
	public Google_file() {
		super();
	}
	
	

	



	public Long getId() {
		return id;
	}







	public void setId(Long id) {
		this.id = id;
	}







	public String getFile_id() {
		return file_id;
	}







	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}







	@Override
	public String toString() {
		return "Google_file [id=" + id + ", file_id=" + file_id + "]";
	}
	
	
	

}
