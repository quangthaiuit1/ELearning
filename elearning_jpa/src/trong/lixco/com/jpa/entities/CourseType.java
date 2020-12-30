package trong.lixco.com.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "course_type")
public class CourseType extends AbstractEntity {
	private String name;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
