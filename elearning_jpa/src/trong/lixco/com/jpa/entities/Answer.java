package trong.lixco.com.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "answer")
public class Answer extends AbstractEntity {
	private String name;
	private boolean is_true = false;
	
	@OneToOne
	private Question question;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIs_true() {
		return is_true;
	}

	public void setIs_true(boolean is_true) {
		this.is_true = is_true;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
}
