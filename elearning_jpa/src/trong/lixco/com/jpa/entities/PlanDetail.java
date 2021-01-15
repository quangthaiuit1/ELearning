package trong.lixco.com.jpa.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import trong.lixco.com.jpa.entity.AbstractEntity;

@Entity
@Table(name = "plan_detail")
public class PlanDetail extends AbstractEntity {
	private double avg_score = 0;
	@OneToOne
	private Course course;
	@OneToOne
	private Plan plan;

	@Temporal(TemporalType.DATE)
	private Date start_time;
	@Temporal(TemporalType.DATE)
	private Date end_time;

	public PlanDetail() {
		super();
	}

	public PlanDetail(Course course, Plan plan, Date start_time, Date end_time) {
		super();
		this.course = course;
		this.plan = plan;
		this.start_time = start_time;
		this.end_time = end_time;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public double getAvg_score() {
		return avg_score;
	}

	public void setAvg_score(double avg_score) {
		this.avg_score = avg_score;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}
}
