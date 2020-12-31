package trong.lixco.com.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "plan_detail_skill")
// chi tiet ke hoach tung ki nang
public class PlanDetailSkill extends AbstractEntity {
	private double score = 0;
	
	@OneToOne
	private Skill skill;
	@OneToOne
	private PlanDetail plan_detail;

	public PlanDetailSkill() {
		super();
	}

	public PlanDetailSkill(Skill skill, PlanDetail plan_detail) {
		super();
		this.skill = skill;
		this.plan_detail = plan_detail;
	}

	public PlanDetailSkill(double score, Skill skill, PlanDetail plan_detail) {
		super();
		this.score = score;
		this.skill = skill;
		this.plan_detail = plan_detail;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public PlanDetail getPlan_detail() {
		return plan_detail;
	}

	public void setPlan_detail(PlanDetail plan_detail) {
		this.plan_detail = plan_detail;
	}
}
