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
	private double score_tuluan = 0;
	private double score_tracnghiem = 0;
	private double score_total = 0;
	private double score_max_tracnghiem = 10;
	private double score_max_tuluan = 0;
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

	public PlanDetailSkill(double score_tuluan, double score_tracnghiem, Skill skill, PlanDetail plan_detail,
			double score_total) {
		super();
		this.score_tuluan = score_tuluan;
		this.score_tracnghiem = score_tracnghiem;
		this.skill = skill;
		this.plan_detail = plan_detail;
		this.score_total = score_total;
	}

	public PlanDetailSkill(double score_tuluan, double score_tracnghiem, double score_total,
			double score_max_tracnghiem, double score_max_tuluan, boolean isSuccess, boolean isExpired, Skill skill,
			PlanDetail plan_detail) {
		super();
		this.score_tuluan = score_tuluan;
		this.score_tracnghiem = score_tracnghiem;
		this.score_total = score_total;
		this.score_max_tracnghiem = score_max_tracnghiem;
		this.score_max_tuluan = score_max_tuluan;
		this.isSuccess = isSuccess;
		this.isExpired = isExpired;
		this.skill = skill;
		this.plan_detail = plan_detail;
	}

	public double getScore_tuluan() {
		return score_tuluan;
	}

	public void setScore_tuluan(double score_tuluan) {
		this.score_tuluan = score_tuluan;
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

	public double getScore_total() {
		return score_total;
	}

	public void setScore_total(double score_total) {
		this.score_total = score_total;
	}

	public double getScore_max_tracnghiem() {
		return score_max_tracnghiem;
	}

	public void setScore_max_tracnghiem(double score_max_tracnghiem) {
		this.score_max_tracnghiem = score_max_tracnghiem;
	}

	public double getScore_max_tuluan() {
		return score_max_tuluan;
	}

	public void setScore_max_tuluan(double score_max_tuluan) {
		this.score_max_tuluan = score_max_tuluan;
	}
}
