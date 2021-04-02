package trong.lixco.com.bean.elearning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.ejb.service.elearning.CourseRatingService;
import trong.lixco.com.ejb.service.elearning.CourseService;
import trong.lixco.com.jpa.entities.Course;
import trong.lixco.com.jpa.entities.CourseRating;

@Named
@ViewScoped
public class CourseDetailBean extends AbstractBean<Course> {

	private static final long serialVersionUID = 1L;
	private Course coursePlaying;
	private List<CourseRating> crsByCouse;

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
		crsByCouse = COURSE_RATING_SERVICE.findByCourse(courseId);
		if (crsByCouse == null) {
			crsByCouse = new ArrayList<>();
		}
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

	public Course getCoursePlaying() {
		return coursePlaying;
	}

	public void setCoursePlaying(Course coursePlaying) {
		this.coursePlaying = coursePlaying;
	}

	public List<CourseRating> getCrsByCouse() {
		return crsByCouse;
	}

	public void setCrsByCouse(List<CourseRating> crsByCouse) {
		this.crsByCouse = crsByCouse;
	}

}
