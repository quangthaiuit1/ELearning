package trong.lixco.com.bean.elearning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.org.xhtmlrenderer.pdf.ITextRenderer;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.bean.staticentity.MessageView;
import trong.lixco.com.ejb.service.elearning.AnswerService;
import trong.lixco.com.ejb.service.elearning.CourseService;
import trong.lixco.com.ejb.service.elearning.CourseTypeService;
import trong.lixco.com.ejb.service.elearning.QuestionService;
import trong.lixco.com.ejb.service.elearning.SkillDetailService;
import trong.lixco.com.ejb.service.elearning.SkillService;
import trong.lixco.com.ejb.service.elearning.StoragePathService;
import trong.lixco.com.jpa.entities.Answer;
import trong.lixco.com.jpa.entities.Course;
import trong.lixco.com.jpa.entities.CourseType;
import trong.lixco.com.jpa.entities.Question;
import trong.lixco.com.jpa.entities.Skill;
import trong.lixco.com.jpa.entities.SkillDetail;
import trong.lixco.com.util.Notify;
import trong.lixco.com.util.PDFMerger;

@Named
@ViewScoped
public class CourseBean extends AbstractBean<Course> {
	private static final long serialVersionUID = 1L;
	private Course courseSelected;
	private List<Course> coursesByType;
	private Course courseNew;
	private Course courseUpdate;
	private Member member;

	private List<CourseType> allCourseType;
	private List<CourseType> courseTypesFilter;
	private CourseType courseTypeSelected;

	private List<Skill> skillsByCourse;
	private List<Skill> skillsFilter;
	private Skill skillSelected;
	private Skill skillNew;
	private Skill skillUpdate;

	private List<SkillDetail> skillDetails;
	private List<SkillDetail> skillDetailsFilter;
	private SkillDetail skillDetailSelected;
	private SkillDetail skillDetailNew;
	private SkillDetail skillDetailUpdate;

	private List<Question> questionsBySkill;
	private Question questionSelected;
	private Question questionNew;
	private Question questionUpdate;

	private List<Answer> answersByQuestion;
	private Answer answerUpdate;
	private Answer answerNew;

	private Notify notify;

	private String pathVideo = "";
	private String pathVideoLocal = "";

	@Inject
	private CourseTypeService COURSE_TYPE_SERVICE;
	@Inject
	private CourseService COURSE_SERVICE;
	@Inject
	private SkillService SKILL_SERVICE;
	@Inject
	private StoragePathService STORAGE_PATH_SERVICE;
	@Inject
	private SkillDetailService SKILL_DETAIL_SERVICE;
	@Inject
	private QuestionService QUESTION_SERVICE;
	@Inject
	private AnswerService ANSWER_SERVICE;

	@Override
	protected void initItem() {
		// pathVideoLocal = STORAGE_PATH_SERVICE.findByName("file").getPath();
		allCourseType = COURSE_TYPE_SERVICE.findAll();
		if (allCourseType == null || allCourseType.isEmpty()) {
			allCourseType = new ArrayList<>();
		}
		courseNew = new Course();
		skillNew = new Skill();
		member = getAccount().getMember();
		answerNew = new Answer();
	}

	public void courseTypeOnRowSelect() {
		coursesByType = COURSE_SERVICE.findByCourseType(courseTypeSelected.getId());
	}

	public void testSkillDetailSelected() {
		System.out.println(skillDetailSelected.getName());
	}

	public void createCourse() {
		courseNew.setCreatedDate(new Date());
		courseNew.setCreatedUser(member.getCode());
		if (courseTypeSelected == null) {
			MessageView.WARN("Vui lòng chọn loại khóa học");
			return;
		}
		courseNew.setCourse_type(courseTypeSelected);
		// check khoa hoc trung ten
		if (StringUtils.isEmpty(courseNew.getName())) {
			MessageView.WARN("Vui lòng điền đầy dủ thông tin");
			return;
		}
		// Course checkExist = COURSE_SERVICE.findByName(courseNew.getName());
		// if (checkExist.getId() != null) {
		// MessageView.WARN("Thông tin đã bị trùng");
		// return;
		// }
		// if (checkExist.getId() == null) {
		Course newEnt = COURSE_SERVICE.create(courseNew);
		if (newEnt != null) {
			if (courseTypeSelected != null) {
				coursesByType = COURSE_SERVICE.findByCourseType(courseTypeSelected.getId());
			}
			MessageView.INFO("Thành công");
			return;
		}
		MessageView.INFO("Lỗi");
		return;
		// }
	}

	public void updateCourse() {
		if (courseUpdate.getId() != 0 || courseUpdate.getId() != null) {
			Course isSuccess = COURSE_SERVICE.update(courseUpdate);
			if (isSuccess != null) {
				MessageView.INFO("Thành công");
			} else {
				MessageView.ERROR("Lỗi");
			}
		}
	}

	public void deleteCourse(Course item) {
		try {
			boolean isSuccess = COURSE_SERVICE.delete(item);
			if (isSuccess) {
				MessageView.INFO("Thành công");
				if (courseTypeSelected != null) {
					coursesByType = COURSE_SERVICE.findByCourseType(courseTypeSelected.getId());
				}
			} else {
				MessageView.ERROR("Lỗi");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void courseOnRowSelect() {
		skillsByCourse = SKILL_SERVICE.findByCourse(courseSelected.getId());
	}

	public void skillOnRowSelect() {
		skillDetails = SKILL_DETAIL_SERVICE.findBySkill(skillSelected.getId());
		skillDetailNew = new SkillDetail();
		// get du lieu cau hoi
		if (skillSelected != null && skillSelected.getId() != null) {
			questionsBySkill = QUESTION_SERVICE.findBySkill(skillSelected.getId());
		}
	}

	public void skillDetailOnRowSelect() {
		pathVideo = skillDetailSelected.getFile_video();
		// if (skillSelected != null && skillSelected.getId() != null) {
		// questionsBySkill =
		// QUESTION_SERVICE.findBySkill(skillSelected.getId());
		// }
	}

	public void questionOnRowSelect() {
		answersByQuestion = ANSWER_SERVICE.findByQuestion(questionSelected.getId());
		// skillDetailNew = new SkillDetail();
	}

	public void createSkill() {
		skillNew.setCreatedDate(new Date());
		skillNew.setCreatedUser(member.getCode());
		if (courseTypeSelected == null || courseSelected == null) {
			MessageView.WARN("Vui lòng thực hiện đúng trình tự");
			return;
		}
		skillNew.setCourse(courseSelected);
		// check khoa hoc trung ten
		if (StringUtils.isEmpty(skillNew.getName())) {
			MessageView.WARN("Vui lòng điền đầy dủ thông tin");
			return;
		}
		Skill checkExist = SKILL_SERVICE.findByName(skillNew.getName());
		if (checkExist.getId() != null) {
			MessageView.WARN("Thông tin đã bị trùng");
			return;
		}
		if (checkExist.getId() == null) {
			Skill newEnt = SKILL_SERVICE.create(skillNew);
			if (newEnt != null) {
				if (courseSelected != null) {
					skillsByCourse = SKILL_SERVICE.findByCourse(courseSelected.getId());
				}
				MessageView.INFO("Thành công");
				return;
			}
			MessageView.INFO("Lỗi");
			return;
		}
	}

	public void updateSkill() {
		if (skillUpdate.getId() != 0 || skillUpdate.getId() != null) {
			Skill isSuccess = SKILL_SERVICE.update(skillUpdate);
			if (isSuccess != null) {
				MessageView.INFO("Thành công");
			} else {
				MessageView.ERROR("Lỗi");
			}
		}
	}

	public void updateSkillDetail() {
		if (skillDetailUpdate.getId() != 0 || skillDetailUpdate.getId() != null) {
			SkillDetail isSuccess = SKILL_DETAIL_SERVICE.update(skillDetailUpdate);
			if (isSuccess != null) {
				MessageView.INFO("Thành công");
			} else {
				MessageView.ERROR("Lỗi");
			}
		}
	}

	public void deleteAnswer(Answer item) {
		try {
			boolean isSuccess = ANSWER_SERVICE.delete(item);
			if (isSuccess) {
				answersByQuestion = ANSWER_SERVICE.findByQuestion(questionSelected.getId());
				MessageView.INFO("Thành công");
			} else {
				MessageView.ERROR("Lỗi");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void assignValueUpdateCourse(Course item) {
		this.courseUpdate = item;
	}

	public void assignValueCreateCourse() {
		this.courseNew = new Course();
	}

	public void assignValueUpdateSkill(Skill item) {
		this.skillUpdate = item;
	}

	public void assignValueUpdateSkillDetail(SkillDetail item) {
		this.skillDetailUpdate = item;
	}

	public void deleteSkill(Skill item) {
		try {
			boolean isSuccess = SKILL_SERVICE.delete(item);
			if (isSuccess) {
				MessageView.INFO("Thành công");
				if (courseSelected != null && courseTypeSelected != null) {
					skillsByCourse = SKILL_SERVICE.findByCourse(courseSelected.getId());
				}
			} else {
				MessageView.ERROR("Lỗi");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteSkillDetail(SkillDetail item) {
		try {
			boolean isSuccess = SKILL_DETAIL_SERVICE.delete(item);
			if (isSuccess) {
				MessageView.INFO("Thành công");
				if (skillSelected != null && courseSelected != null) {
					skillDetails = SKILL_DETAIL_SERVICE.findBySkill(skillSelected.getId());
				}
			} else {
				MessageView.ERROR("Lỗi");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteQuestion(Question item) {
		try {
			QUESTION_SERVICE.delete(item);
			MessageView.INFO("Thành công");
			if (skillSelected != null && skillSelected.getId() != null) {
				questionsBySkill = QUESTION_SERVICE.findBySkill(skillSelected.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageView.ERROR("Lỗi");
		}
	}

	private String nameAssign;

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

	// public void selectData(KPIPersonOfMonth kpm) {
	// this.kpm = kpm;
	// }

	private boolean orverideData = false;

	// Cap nhat dư liêu chung minh
	public void updateDataAssign(FileUploadEvent event) {
		notify = new Notify(FacesContext.getCurrentInstance());
		if (allowUpdate(null)) {
			try (InputStream input = event.getFile().getInputstream()) {
				byte[] file = IOUtils.toByteArray(input);
				byte[] fileold = PDFMerger.getFile(STORAGE_PATH_SERVICE.findByName("file").getPath(),
						skillDetailSelected.getFile_document());
				if (!orverideData) {
					if (fileold != null) {
						file = PDFMerger.mergePdfs(fileold, file);
					}
				}
				if (file == null) {
					notice("File vượt quá dung lượng.");
				} else {
					PDFMerger.getDelete(STORAGE_PATH_SERVICE.findByName("file").getPath(),
							skillDetailSelected.getFile_document());
					String filename = System.currentTimeMillis() + "-" + skillDetailSelected.getId() + ".pdf";
					File directory = new File(STORAGE_PATH_SERVICE.findByName("file").getPath(), filename);
					FileUtils.writeByteArrayToFile(directory, file);
					skillDetailSelected.setFile_document(filename);
					skillDetailSelected = SKILL_DETAIL_SERVICE.update(skillDetailSelected);
					// kpiPersonOfMonths.set(kpiPersonOfMonths.indexOf(kpm),
					// this.kpm);
					PrimeFaces.current().executeScript("PF('dialogDataAssign').hide();");
					notice("Tải lên thành công.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				// kpm = kPIPersonOfMonthService.findById(kpm.getId());
				// kpiPersonOfMonths.set(kpiPersonOfMonths.indexOf(kpm),
				// this.kpm);
				MessageView.ERROR("Không lưu được. Dữ liệu vượt quá dung lượng");
			}
		} else {
			MessageView.ERROR("Tài khoản không có quyền thực hiện.");
		}
	}

	public void updateDataVideo(FileUploadEvent event) {
		notify = new Notify(FacesContext.getCurrentInstance());

		try {
			InputStream in = event.getFile().getInputstream();
			// Ghi inputStream vao 1 OutputStream
			// Tao outputstream -> new doi tuong xong truyen duong dan +
			// fileName
			String filename = System.currentTimeMillis() + "-" + skillDetailSelected.getId() + ".mp4";
			OutputStream out = new FileOutputStream(
					new File(STORAGE_PATH_SERVICE.findByName("video").getPath() + filename));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();
			System.err.println("New file created!");

			skillDetailSelected.setFile_video(filename);
			skillDetailSelected = SKILL_DETAIL_SERVICE.update(skillDetailSelected);
			PrimeFaces.current().executeScript("PF('dialogDataVideo').hide();");
			notice("Tải lên thành công.");

		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			MessageView.ERROR("Không lưu được. Dữ liệu vượt quá dung lượng");
		}

		// if (allowUpdate(null)) {
		// try (InputStream input = event.getFile().getInputstream()) {
		// byte[] file = IOUtils.toByteArray(input);
		// byte[] fileold =
		// PDFMerger.getFile(STORAGE_PATH_SERVICE.findByName("video").getPath(),
		// skillSelected.getFile_video());
		// if (!orverideData) {
		// if (fileold != null) {
		// file = PDFMerger.mergePdfs(fileold, file);
		// }
		// }
		// if (file == null) {
		// notice("File vượt quá dung lượng.");
		// } else {
		// PDFMerger.getDelete(STORAGE_PATH_SERVICE.findByName("video").getPath(),
		// skillSelected.getFile_video());
		// String filename = System.currentTimeMillis() + "-" +
		// skillSelected.getId() + ".mp4";
		// File directory = new
		// File(STORAGE_PATH_SERVICE.findByName("video").getPath(), filename);
		// FileUtils.writeByteArrayToFile(directory, file);
		// skillSelected.setFile_video(filename);
		// skillSelected = SKILL_SERVICE.update(skillSelected);
		// // kpiPersonOfMonths.set(kpiPersonOfMonths.indexOf(kpm),
		// // this.kpm);
		// PrimeFaces.current().executeScript("PF('dialogDataVideo').hide();");
		// notice("Tải lên thành công.");
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// // kpm = kPIPersonOfMonthService.findById(kpm.getId());
		// // kpiPersonOfMonths.set(kpiPersonOfMonths.indexOf(kpm),
		// // this.kpm);
		// MessageView.ERROR("Không lưu được. Dữ liệu vượt quá dung lượng");
		// }
		// } else {
		// MessageView.ERROR("Tài khoản không có quyền thực hiện.");
		// }
	}

	public void showPDFData() throws IOException {
		if (skillDetailSelected != null && skillDetailSelected.getId() != null) {
			notify = new Notify(FacesContext.getCurrentInstance());
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

	public void createSkillDetail() {
		skillDetailNew.setCreatedDate(new Date());
		skillDetailNew.setCreatedUser(member.getCode());
		if (skillSelected == null || courseSelected == null) {
			MessageView.WARN("Vui lòng thực hiện đúng trình tự");
			return;
		}
		skillDetailNew.setSkill(skillSelected);
		// check khoa hoc trung ten
		if (StringUtils.isEmpty(skillDetailNew.getName())) {
			MessageView.WARN("Vui lòng điền đầy dủ thông tin");
			return;
		}
		SkillDetail checkExist = SKILL_DETAIL_SERVICE.findByName(skillDetailNew.getName());
		if (checkExist.getId() != null) {
			MessageView.WARN("Thông tin đã bị trùng");
			return;
		}
		if (checkExist.getId() == null) {
			SkillDetail newEnt = SKILL_DETAIL_SERVICE.create(skillDetailNew);
			if (newEnt != null) {
				if (skillSelected != null) {
					skillDetails = SKILL_DETAIL_SERVICE.findBySkill(skillSelected.getId());
				}
				MessageView.INFO("Thành công");
				skillDetailNew = new SkillDetail();
				return;
			}
			MessageView.INFO("Lỗi");
			return;
		}
	}

	public void createAnswer() {
		if (questionSelected == null || questionSelected.getId() == null) {
			MessageView.ERROR("Lỗi. Chưa chọn câu hỏi");
			return;
		}
		answerNew.setCreatedDate(new Date());
		answerNew.setCreatedUser(member.getCode());
		answerNew.setQuestion(questionSelected);
		answerNew = ANSWER_SERVICE.create(answerNew);
		if (answerNew.getId() != null && answerNew.getId() != 0) {
			answersByQuestion = ANSWER_SERVICE.findByQuestion(questionSelected.getId());
			MessageView.INFO("Thành công");
			answerNew = new Answer();
			return;
		}
		if (answerNew.getId() == null || answerNew.getId() == 0) {
			MessageView.ERROR("Lỗi");
		}
	}

	// test video
	private String events = "";

	public String getEvents() {
		return events;
	}

	public void setEvents(String events) {
		this.events = events;
	}

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

	public Course getCourseSelected() {
		return courseSelected;
	}

	public void setCourseSelected(Course courseSelected) {
		this.courseSelected = courseSelected;
	}

	public List<CourseType> getAllCourseType() {
		return allCourseType;
	}

	public void setAllCourseType(List<CourseType> allCourseType) {
		this.allCourseType = allCourseType;
	}

	public CourseType getCourseTypeSelected() {
		return courseTypeSelected;
	}

	public void setCourseTypeSelected(CourseType courseTypeSelected) {
		this.courseTypeSelected = courseTypeSelected;
	}

	public List<CourseType> getCourseTypesFilter() {
		return courseTypesFilter;
	}

	public void setCourseTypesFilter(List<CourseType> courseTypesFilter) {
		this.courseTypesFilter = courseTypesFilter;
	}

	public List<Course> getCoursesByType() {
		return coursesByType;
	}

	public void setCoursesByType(List<Course> coursesByType) {
		this.coursesByType = coursesByType;
	}

	public Course getCourseNew() {
		return courseNew;
	}

	public void setCourseNew(Course courseNew) {
		this.courseNew = courseNew;
	}

	public Course getCourseUpdate() {
		return courseUpdate;
	}

	public void setCourseUpdate(Course courseUpdate) {
		this.courseUpdate = courseUpdate;
	}

	public List<Skill> getSkillsByCourse() {
		return skillsByCourse;
	}

	public void setSkillsByCourse(List<Skill> skillsByCourse) {
		this.skillsByCourse = skillsByCourse;
	}

	public List<Skill> getSkillsFilter() {
		return skillsFilter;
	}

	public void setSkillsFilter(List<Skill> skillsFilter) {
		this.skillsFilter = skillsFilter;
	}

	public Skill getSkillSelected() {
		return skillSelected;
	}

	public void setSkillSelected(Skill skillSelected) {
		this.skillSelected = skillSelected;
	}

	public Skill getSkillNew() {
		return skillNew;
	}

	public void setSkillNew(Skill skillNew) {
		this.skillNew = skillNew;
	}

	public Skill getSkillUpdate() {
		return skillUpdate;
	}

	public void setSkillUpdate(Skill skillUpdate) {
		this.skillUpdate = skillUpdate;
	}

	public boolean isOrverideData() {
		return orverideData;
	}

	public void setOrverideData(boolean orverideData) {
		this.orverideData = orverideData;
	}

	public String getPathVideo() {
		return pathVideo;
	}

	public void setPathVideo(String pathVideo) {
		this.pathVideo = pathVideo;
	}

	public List<SkillDetail> getSkillDetails() {
		return skillDetails;
	}

	public void setSkillDetails(List<SkillDetail> skillDetails) {
		this.skillDetails = skillDetails;
	}

	public List<SkillDetail> getSkillDetailsFilter() {
		return skillDetailsFilter;
	}

	public void setSkillDetailsFilter(List<SkillDetail> skillDetailsFilter) {
		this.skillDetailsFilter = skillDetailsFilter;
	}

	public SkillDetail getSkillDetailSelected() {
		return skillDetailSelected;
	}

	public void setSkillDetailSelected(SkillDetail skillDetailSelected) {
		this.skillDetailSelected = skillDetailSelected;
	}

	public SkillDetail getSkillDetailNew() {
		return skillDetailNew;
	}

	public void setSkillDetailNew(SkillDetail skillDetailNew) {
		this.skillDetailNew = skillDetailNew;
	}

	public SkillDetail getSkillDetailUpdate() {
		return skillDetailUpdate;
	}

	public void setSkillDetailUpdate(SkillDetail skillDetailUpdate) {
		this.skillDetailUpdate = skillDetailUpdate;
	}

	public List<Question> getQuestionsBySkill() {
		return questionsBySkill;
	}

	public void setQuestionsBySkill(List<Question> questionsBySkill) {
		this.questionsBySkill = questionsBySkill;
	}

	public Question getQuestionSelected() {
		return questionSelected;
	}

	public void setQuestionSelected(Question questionSelected) {
		this.questionSelected = questionSelected;
	}

	public Question getQuestionNew() {
		return questionNew;
	}

	public void setQuestionNew(Question questionNew) {
		this.questionNew = questionNew;
	}

	public Question getQuestionUpdate() {
		return questionUpdate;
	}

	public void setQuestionUpdate(Question questionUpdate) {
		this.questionUpdate = questionUpdate;
	}

	public List<Answer> getAnswersByQuestion() {
		return answersByQuestion;
	}

	public void setAnswersByQuestion(List<Answer> answersByQuestion) {
		this.answersByQuestion = answersByQuestion;
	}

	public Answer getAnswerUpdate() {
		return answerUpdate;
	}

	public void setAnswerUpdate(Answer answerUpdate) {
		this.answerUpdate = answerUpdate;
	}

	public Answer getAnswerNew() {
		return answerNew;
	}

	public void setAnswerNew(Answer answerNew) {
		this.answerNew = answerNew;
	}
}
