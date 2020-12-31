package trong.lixco.com.bean.entities;

import java.util.List;

import trong.lixco.com.jpa.entities.Answer;
import trong.lixco.com.jpa.entities.Question;

public class QuestionAndAnswer {
	private Question question;
	private List<Answer> answers;

	public QuestionAndAnswer() {
		super();
	}

	public QuestionAndAnswer(Question question, List<Answer> answers) {
		super();
		this.question = question;
		this.answers = answers;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

}
