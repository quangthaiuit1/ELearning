package trong.lixco.com.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "course_position_job")
public class CoursePositionJob extends AbstractEntity {
	private String position_job_code;
	@OneToOne
	private Course course;

	public CoursePositionJob() {
		super();
	}

	public CoursePositionJob(String position_job_code, Course course) {
		super();
		this.position_job_code = position_job_code;
		this.course = course;
	}

	public String getPosition_job_code() {
		return position_job_code;
	}

	public void setPosition_job_code(String position_job_code) {
		this.position_job_code = position_job_code;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}
