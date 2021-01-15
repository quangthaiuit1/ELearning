package trong.lixco.com.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "log_time_seen_video")
public class LogTimeSeenVideo extends AbstractEntity {
	private int time_seen_second;

	@OneToOne
	private PlanDetailSkill plan_detail_skill;
	@OneToOne
	private SkillDetail skill_detail;

	public int getTime_seen_second() {
		return time_seen_second;
	}

	public void setTime_seen_second(int time_seen_second) {
		this.time_seen_second = time_seen_second;
	}

	public PlanDetailSkill getPlan_detail_skill() {
		return plan_detail_skill;
	}

	public void setPlan_detail_skill(PlanDetailSkill plan_detail_skill) {
		this.plan_detail_skill = plan_detail_skill;
	}

	public SkillDetail getSkill_detail() {
		return skill_detail;
	}

	public void setSkill_detail(SkillDetail skill_detail) {
		this.skill_detail = skill_detail;
	}
}
