package trong.lixco.com.bean.elearning;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
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
import trong.lixco.com.bean.staticentity.MessageView;
import trong.lixco.com.ejb.service.elearning.CourseService;
import trong.lixco.com.ejb.service.elearning.PlanDetailService;
import trong.lixco.com.ejb.service.elearning.PlanDetailSkillService;
import trong.lixco.com.ejb.service.elearning.PlanService;
import trong.lixco.com.ejb.service.elearning.SkillDetailService;
import trong.lixco.com.ejb.service.elearning.SkillService;
import trong.lixco.com.ejb.service.elearning.StoragePathService;
import trong.lixco.com.ejb.service.elearning.TestResultService;
import trong.lixco.com.jpa.entities.Course;
import trong.lixco.com.jpa.entities.Plan;
import trong.lixco.com.jpa.entities.PlanDetail;
import trong.lixco.com.jpa.entities.PlanDetailSkill;
import trong.lixco.com.jpa.entities.Skill;
import trong.lixco.com.jpa.entities.SkillDetail;
import trong.lixco.com.jpa.entities.TestResult;
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
	private List<SkillDetail> skillDetailsBySkill;
	private SkillDetail skillDetailSelected;
	private String pathVideo = "";
	private List<PlanDetailSkill> pdSkillsByPD; // planDetailSkill by planDetail
	private PlanDetailSkill pdSkillSelected;
	private double avgCourse = 0;
	private long idPlanDetail;
	private PlanDetail pdPlaying;

	@Inject
	private CourseService COURSE_SERVICE;
	@Inject
	private SkillService SKILL_SERVICE;
	@Inject
	private SkillDetailService SKILL_DETAIL_SERVICE;
	@Inject
	private StoragePathService STORAGE_PATH_SERVICE;
	@Inject
	private PlanDetailSkillService PlAN_DETAIL_SKILL_SERVICE;
	@Inject
	private PlanDetailService PLAN_DETAIL_SERVICE;
	@Inject
	private TestResultService TEST_RESULT_SERVICE;

	// end use
	@Override
	protected void initItem() {
		try {
			member = getAccount().getMember();
			idPlanDetail = getPlanDetailId();

			if (idPlanDetail != 0) {
				pdPlaying = PLAN_DETAIL_SERVICE.findById(idPlanDetail);
				if (pdPlaying != null) {
					avgCourse = pdPlaying.getAvg_score();
					pdSkillsByPD = PlAN_DETAIL_SKILL_SERVICE.findBySkillAndPlanDetail(0, idPlanDetail);
					// kiem tra thu bai test nao da hoan thanh
					for (int i = 0; i < pdSkillsByPD.size(); i++) {
						List<TestResult> ts = TEST_RESULT_SERVICE.findByPDSId(pdSkillsByPD.get(i).getId());
						if (!ts.isEmpty()) {
							pdSkillsByPD.get(i).setSuccess(true);
						}
						// kiem tra khoa hoc da het han hay chua
						Date currentDate = new Date();
						if (currentDate.after(pdSkillsByPD.get(i).getPlan_detail().getEnd_time())
								|| currentDate.before(pdSkillsByPD.get(i).getPlan_detail().getStart_time())) {
							pdSkillsByPD.get(i).setExpired(true);
						}
					}
				}
			} else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// kiem tra xem bai thi da duoc thuc hien hay chua
	public void checkTestSuccess() {

	}

	public long getPlanDetailId() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String setofIdTemp = request.getParameter("pdid");
		if (setofIdTemp == null || setofIdTemp.equals("null") || setofIdTemp.equals("")) {
			return 0;
		}
		return Long.parseLong(setofIdTemp);
	}

	public void skillOnRowSelect() {
		// skillDetailsBySkill =
		// SKILL_DETAIL_SERVICE.findBySkill(pdSkillSelected.getSkill().getId());
		try {
			FacesContext fContext = FacesContext.getCurrentInstance();
			ExternalContext extContext = fContext.getExternalContext();
			HttpServletRequest ht = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			String path = "http://" + ht.getServerName() + ":" + ht.getServerPort()
					+ "/elearning/pages/nhanvien/thongtinkhoahoc.htm?pdsid=" + pdSkillSelected.getSkill().getId();
			extContext.redirect(path);
		} catch (IOException e) {
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

	// public void handleAVGCourse() {
	// double totalScore = 0;
	// List<PlanDetailSkill> pdsByPD =
	// PlAN_DETAIL_SKILL_SERVICE.findBySkillAndPlanDetail(0, idPlanDetail);
	// if (!pdsByPD.isEmpty() && pdsByPD != null) {
	// for (PlanDetailSkill p : pdsByPD) {
	// totalScore = totalScore + p.getScore();
	// }
	// avgCourse = totalScore / (double) pdsByPD.size();
	// PlanDetail pd = pdsByPD.get(0).getPlan_detail();
	// pd.setAvg_score(avgCourse);
	// PlanDetail p = PLAN_DETAIL_SERVICE.update(pd);
	// if (p != null && p.getId() != null) {
	// MessageView.INFO("Thành công");
	// return;
	// } else {
	// MessageView.ERROR("Lỗi");
	// return;
	// }
	// }
	// }

	public void saveOrUpdateTab3() {

	}

	public void create() {

	}

	public void delete() {

	}

	public void skillDetailOnRowSelect() {
		pathVideo = skillDetailSelected.getFile_video();
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

	private String events = "";

	public void onPlay() {
		events = "play" + "\n" + events;
	}

	public void onPause() {
		events = "pause" + "\n" + events;
	}

	public void onSeeking() {
		events = "seeking" + "\n" + events;
	}

	public void onCanplaythrough() {
		events = "can play through" + "\n" + events;
	}

	public void onLoadeddata() {
		events = "loaded data" + "\n" + events;
	}

	public void onLoadeddataNew() {
		events = "loaded data" + "\n" + events;
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

	public String getPathVideo() {
		return pathVideo;
	}

	public void setPathVideo(String pathVideo) {
		this.pathVideo = pathVideo;
	}

	public List<PlanDetailSkill> getPdSkillsByPD() {
		return pdSkillsByPD;
	}

	public void setPdSkillsByPD(List<PlanDetailSkill> pdSkillsByPD) {
		this.pdSkillsByPD = pdSkillsByPD;
	}

	public PlanDetailSkill getPdSkillSelected() {
		return pdSkillSelected;
	}

	public void setPdSkillSelected(PlanDetailSkill pdSkillSelected) {
		this.pdSkillSelected = pdSkillSelected;
	}

	public double getAvgCourse() {
		return avgCourse;
	}

	public void setAvgCourse(double avgCourse) {
		this.avgCourse = avgCourse;
	}
}
