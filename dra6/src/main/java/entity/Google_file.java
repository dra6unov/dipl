package entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Google_file {
	@Id
	private Long id;
	private String file_id;
	
	@OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
	private User_file user_file;

	public Google_file() {
		super();
	}
	



	public Google_file(Long id, String file_id) {
		this.id = id;
		this.file_id = file_id;
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
