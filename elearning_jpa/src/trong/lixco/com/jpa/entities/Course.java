package trong.lixco.com.jpa.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "course")
public class Course extends AbstractEntity {
	private String name;
	private String description;
	private boolean is_optional = false;
	private int time; // thoi luong khoa hoc - tinh bang ngay

	@Transient
	private Date start_date;
	@Transient
	private Date end_date;

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

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
