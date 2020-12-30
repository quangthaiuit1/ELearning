package trong.lixco.com.bean.elearning;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.docx4j.org.xhtmlrenderer.pdf.ITextRenderer;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.account.servicepublics.MemberServicePublic;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.ejb.service.elearning.CourseService;
import trong.lixco.com.ejb.service.elearning.PlanDetailService;
import trong.lixco.com.ejb.service.elearning.PlanService;
import trong.lixco.com.ejb.service.elearning.SkillDetailService;
import trong.lixco.com.ejb.service.elearning.SkillService;
import trong.lixco.com.ejb.service.elearning.StoragePathService;
import trong.lixco.com.jpa.entities.Course;
import trong.lixco.com.jpa.entities.Plan;
import trong.lixco.com.jpa.entities.Skill;
import trong.lixco.com.jpa.entities.SkillDetail;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.PDFMerger;

@Named
@ViewScoped
public class CourseEmployeeDetailBean extends AbstractBean<Course> {
	private static final long serialVersionUID = 1L;
	private Member member;

	DepartmentServicePublic departmentServicePublic;
	MemberServicePublic memberServicePublic;

	// Use
	private List<Course> coursesByEmpl;
	private Course courseSelected;
	private List<Skill> skillsByCourse;
	private Skill skillSelected;
	private List<SkillDetail> skillDetailsBySkill;
	private SkillDetail skillDetailSelected;
	@Inject
	private CourseService COURSE_SERVICE;
	@Inject
	private SkillService SKILL_SERVICE;
	@Inject
	private SkillDetailService SKILL_DETAIL_SERVICE;
	@Inject
	private StoragePathService STORAGE_PATH_SERVICE;
	// end use

	@Override
	protected void initItem() {
		try {
			member = getAccount().getMember();
			long idCourseSelected = getParamCourseId();
			if (idCourseSelected != 0) {
				courseSelected = COURSE_SERVICE.findById(idCourseSelected);
				skillsByCourse = SKILL_SERVICE.findByCourse(courseSelected.getId());
			} else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public long getParamCourseId() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String setofIdTemp = request.getParameter("id");
		return Long.parseLong(setofIdTemp);
	}

	public void skillOnRowSelect() {
		try {
			skillDetailsBySkill = SKILL_DETAIL_SERVICE.findBySkill(skillSelected.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processDataAssign() {
		if (skillDetailSelected != null) {
			byte[] file = PDFMerger.getFile(STORAGE_PATH_SERVICE.findByName("file").getPath(),
					this.skillDetailSelected.getFile_document());
			if (file != null) {
				PrimeFaces.current().executeScript("document.getElementById('formdataassign:process').click();");
			} else {
				notice("Không có dữ liệu chứng minh.");
			}
		} else {
			notice("Lưu dữ liệu trước khi nạp dữ liệu chứng minh.");
		}
	}

	public void saveOrUpdateTab3() {

	}

	public void create() {

	}

	public void removeKPIPersonOther() {

	}

	public void delete() {

	}

	public void showPDFData() throws IOException {
		if (skillDetailSelected != null && skillDetailSelected.getId() != null) {
			try {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				ExternalContext externalContext = facesContext.getExternalContext();
				try {
					byte[] file = PDFMerger.getFile(STORAGE_PATH_SERVICE.findByName("file").getPath(),
							skillDetailSelected.getFile_document());
					HttpSession session = (HttpSession) externalContext.getSession(true);
					HttpServletRequest ht = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
							.getRequest();
					ITextRenderer renderer = new ITextRenderer();
					String url = "http://" + ht.getServerName() + ":" + ht.getServerPort()
							+ "/elearning/showdata.xhtml;jsessionid=" + session.getId() + "?pdf=true";
					renderer.setDocument(new URL(url).toString());
					renderer.layout();

					HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
					response.setContentType("application/pdf");
					response.addHeader("Content-disposition", "inline;filename=report.pdf");

					response.setContentLength(file.length);
					response.getOutputStream().write(file, 0, file.length);
					response.getOutputStream().flush();

					OutputStream browserStream = response.getOutputStream();
					renderer.createPDF(browserStream);
				} finally {
					facesContext.responseComplete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

	public List<Course> getCoursesByEmpl() {
		return coursesByEmpl;
	}

	public void setCoursesByEmpl(List<Course> coursesByEmpl) {
		this.coursesByEmpl = coursesByEmpl;
	}

	public Course getCourseSelected() {
		return courseSelected;
	}

	public void setCourseSelected(Course courseSelected) {
		this.courseSelected = courseSelected;
	}

	public List<Skill> getSkillsByCourse() {
		return skillsByCourse;
	}

	public void setSkillsByCourse(List<Skill> skillsByCourse) {
		this.skillsByCourse = skillsByCourse;
	}

	public Skill getSkillSelected() {
		return skillSelected;
	}

	public void setSkillSelected(Skill skillSelected) {
		this.skillSelected = skillSelected;
	}

	public List<SkillDetail> getSkillDetailsBySkill() {
		return skillDetailsBySkill;
	}

	public void setSkillDetailsBySkill(List<SkillDetail> skillDetailsBySkill) {
		this.skillDetailsBySkill = skillDetailsBySkill;
	}

	public SkillDetail getSkillDetailSelected() {
		return skillDetailSelected;
	}

	public void setSkillDetailSelected(SkillDetail skillDetailSelected) {
		this.skillDetailSelected = skillDetailSelected;
	}

	// use

}
