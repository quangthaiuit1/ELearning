package trong.lixco.com.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "course")
public class Course extends AbstractEntity {
	private String name;
	private String description;
	private boolean is_optional = false;

	@OneToOne
	private CourseType course_type;

	public boolean isIs_optional() {
		return is_optional;
	}

	public void setIs_optional(boolean is_optional) {
		this.is_optional = is_optional;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CourseType getCourse_type() {
		return course_type;
	}

	public void setCourse_type(CourseType course_type) {
		this.course_type = course_type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
