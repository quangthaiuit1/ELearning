package trong.lixco.com.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "storage_path")
public class StoragePath extends AbstractEntity {
	private String name;
	private String path;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
