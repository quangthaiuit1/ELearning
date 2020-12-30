package trong.lixco.com.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "question")
public class Question extends AbstractEntity {
	private String name_question;
	// private String answer;
	// private boolean is_true = false;

	@OneToOne
	private Skill skill;

	@OneToOne
	private QuestionType question_type;

	public String getName_question() {
		return name_question;
	}

	public void setName_question(String name_question) {
		this.name_question = name_question;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public QuestionType getQuestion_type() {
		return question_type;
	}

	public void setQuestion_type(QuestionType question_type) {
		this.question_type = question_type;
	}
}
