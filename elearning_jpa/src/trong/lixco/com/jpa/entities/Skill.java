package trong.lixco.com.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "skill")
public class Skill extends AbstractEntity {
	private String name;
	private String description;
	private String file_document;// file tài liệu
	private String file_video;

	@OneToOne
	private Course course;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFile_document() {
		return file_document;
	}

	public void setFile_document(String file_document) {
		this.file_document = file_document;
	}

	public String getFile_video() {
		return file_video;
	}

	public void setFile_video(String file_video) {
		this.file_video = file_video;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
