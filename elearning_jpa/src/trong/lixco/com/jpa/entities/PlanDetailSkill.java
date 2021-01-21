package trong.lixco.com.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "plan_detail_skill")
// chi tiet ke hoach tung ki nang
public class PlanDetailSkill extends AbstractEntity {
	// private double score_tuluan = 0;
	private double score_tracnghiem = 0;
	// private double score_total = 0;
	// private double score_max_tracnghiem = 10;
	// private double score_max_tuluan = 0;
	@Transient
	private boolean isSuccess = false;
	@Transient
	private boolean isExpired = false;

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

	public PlanDetailSkill(double score_tracnghiem, Skill skill, PlanDetail plan_detail) {
		super();
		this.score_tracnghiem = score_tracnghiem;
		this.skill = skill;
		this.plan_detail = plan_detail;
	}

	public PlanDetailSkill(double score_tracnghiem, boolean isSuccess, boolean isExpired, Skill skill,
			PlanDetail plan_detail) {
		super();
		this.score_tracnghiem = score_tracnghiem;
		this.isSuccess = isSuccess;
		this.isExpired = isExpired;
		this.skill = skill;
		this.plan_detail = plan_detail;
	}

	public double getScore_tracnghiem() {
		return score_tracnghiem;
	}

	public void setScore_tracnghiem(double score_tracnghiem) {
		this.score_tracnghiem = score_tracnghiem;
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

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}

}
