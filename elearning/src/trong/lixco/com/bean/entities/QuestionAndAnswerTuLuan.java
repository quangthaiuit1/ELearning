package trong.lixco.com.bean.entities;

import trong.lixco.com.jpa.entities.Question;

public class QuestionAndAnswerTuLuan {
	private Question question;
	private String answer = "";

	public QuestionAndAnswerTuLuan() {
		super();
	}

	public QuestionAndAnswerTuLuan(Question question, String answer) {
		super();
		this.question = question;
		this.answer = answer;
	}

	public QuestionAndAnswerTuLuan(Question question) {
		super();
		this.question = question;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
