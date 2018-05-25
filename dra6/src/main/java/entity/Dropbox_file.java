package entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Dropbox_file {
	
	@Id
	private Long id;
	private String file_path;
	
	public Dropbox_file() {
	}

	public Dropbox_file(Long id, String file_path) {
		this.id = id;
		this.file_path = file_path;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}


	@Override
	public String toString() {
		return "Dropbox_file [id=" + id + ", file_path=" + file_path + "]";
	}
	
	

}
