package trong.lixco.com.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "test_result")
public class TestResult extends AbstractEntity {
	private String employee_code;
	@OneToOne
	private Answer answer;
	@OneToOne
	private PlanDetailSkill plan_detail_skill;

	public TestResult() {
		super();
	}

	public String getEmployee_code() {
		return employee_code;
	}

	public void setEmployee_code(String employee_code) {
		this.employee_code = employee_code;
	}

	public TestResult(String employee_code, Answer answer, PlanDetailSkill plan_detail_skill) {
		super();
		this.employee_code = employee_code;
		this.answer = answer;
		this.plan_detail_skill = plan_detail_skill;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public PlanDetailSkill getPlan_detail_skill() {
		return plan_detail_skill;
	}

	public void setPlan_detail_skill(PlanDetailSkill plan_detail_skill) {
		this.plan_detail_skill = plan_detail_skill;
	}
}
