package trong.lixco.com.bean.elearning;

import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.bean.staticentity.MessageView;
import trong.lixco.com.ejb.service.elearning.CourseRatingService;
import trong.lixco.com.ejb.service.elearning.CourseService;
import trong.lixco.com.jpa.entities.Course;
import trong.lixco.com.jpa.entities.CourseRating;

@Named
@ViewScoped
public class ExcuteRateBean extends AbstractBean<CourseRating> {

	private static final long serialVersionUID = 1L;
	private Course coursePlaying;
	private Course courseNew;
	private CourseRating crNew;
	private Member member;

	@Inject
	private CourseService COURSE_SEVRVICE;
	@Inject
	private CourseRatingService COURSE_RATING_SERVICE;

	@Override
	protected void initItem() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		String param1 = params.get("course");
		long courseId = 0;
		if (param1 != null) {
			courseId = Long.parseLong(param1);
		}
		coursePlaying = COURSE_SEVRVICE.findById(courseId);
		member = getAccount().getMember();
		// check da exist chua
		List<CourseRating> checkExist = COURSE_RATING_SERVICE.findByCourseAndEmpl(coursePlaying.getId(),
				member.getCode());
		if (checkExist.isEmpty()) {
			crNew = new CourseRating();
		} else {
			crNew = checkExist.get(0);
		}
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

	public void createOrUpdate() {
		// check da exist chua
		List<CourseRating> checkExist = COURSE_RATING_SERVICE.findByCourseAndEmpl(coursePlaying.getId(),
				member.getCode());
		if (checkExist.isEmpty()) {
			// them moi
			crNew.setCourse(coursePlaying);
			crNew.setEmployee_code(member.getCode());
			crNew.setEmployee_name(member.getName());
			crNew.setDepartment_code(member.getDepartment().getCode());
			crNew.setDepartment_name(member.getDepartment().getName());
			try {
				COURSE_RATING_SERVICE.create(crNew);
				MessageView.INFO("Thành công");
			} catch (Exception e) {
				e.printStackTrace();
				MessageView.ERROR("Lỗi");
			}
		}
		// sua
		else {
			try {
				COURSE_RATING_SERVICE.update(crNew);
				MessageView.INFO("Cập nhật thành công");
			} catch (Exception e) {
				e.printStackTrace();
				MessageView.ERROR("Cập nhật không thành công");
			}
		}

	}

	public Course getCourseNew() {
		return courseNew;
	}

	public void setCourseNew(Course courseNew) {
		this.courseNew = courseNew;
	}

	public Course getCoursePlaying() {
		return coursePlaying;
	}

	public void setCoursePlaying(Course coursePlaying) {
		this.coursePlaying = coursePlaying;
	}

	public CourseRating getCrNew() {
		return crNew;
	}

	public void setCrNew(CourseRating crNew) {
		this.crNew = crNew;
	}
}
