package trong.lixco.com.bean.elearning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.DepartmentServicePublicProxy;
import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.account.servicepublics.MemberServicePublic;
import trong.lixco.com.account.servicepublics.MemberServicePublicProxy;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.bean.entities.PositionJobData;
import trong.lixco.com.bean.entities.PositionJobDataService;
import trong.lixco.com.bean.staticentity.DateUtil;
import trong.lixco.com.bean.staticentity.MessageView;
import trong.lixco.com.ejb.service.elearning.CoursePositionJobService;
import trong.lixco.com.ejb.service.elearning.CourseService;
import trong.lixco.com.ejb.service.elearning.PlanDetailService;
import trong.lixco.com.ejb.service.elearning.PlanDetailSkillService;
import trong.lixco.com.ejb.service.elearning.PlanService;
import trong.lixco.com.ejb.service.elearning.SkillService;
import trong.lixco.com.jpa.entities.Course;
import trong.lixco.com.jpa.entities.CoursePositionJob;
import trong.lixco.com.jpa.entities.Plan;
import trong.lixco.com.jpa.entities.PlanDetail;
import trong.lixco.com.jpa.entities.PlanDetailSkill;
import trong.lixco.com.jpa.entities.Skill;
import trong.lixco.com.servicepublic.EmployeeDTO;
import trong.lixco.com.servicepublic.EmployeeServicePublic;
import trong.lixco.com.servicepublic.EmployeeServicePublicProxy;
import trong.lixco.com.util.DepartmentUtil;

@Named
@ViewScoped
public class CourseEmployeeBean extends AbstractBean<Course> {
	private static final long serialVersionUID = 1L;
	private int yearSearch = 0;
	private Department departmentSelected;
	private List<Department> departmentSearchs;
	private List<Plan> plansByDepart;
	private List<Plan> plansFilter;
	private Plan planSelected;
	private Member member;
	private PositionJobData[] positionsByEmpl;
	private List<PositionJobData> positionsByEmplList;
	private PositionJobData positionSelected;
	private List<Course> coursesByPosition;
	private List<Course> allCourse;
	private List<PlanDetail> detailsByPlan;
	private List<PlanDetail> planDetailsComing;
	private PlanDetail planDetailSelected;

	DepartmentServicePublic departmentServicePublic;
	MemberServicePublic memberServicePublic;
	private EmployeeServicePublic EMPLOYEE_SERVICE_PUBLIC;

	@Inject
	private PlanService PLAN_SERVICE;
	@Inject
	private CoursePositionJobService COURSE_POSITION_JOB_SERVICE;
	@Inject
	private CourseService COURSE_SERVICE;
	@Inject
	private PlanDetailService PLAN_DETAIL_SERVICE;
	@Inject
	private SkillService SKILL_SERVICE;
	@Inject
	private PlanDetailSkillService PLAN_DETAIL_SKILL_SERVICE;

	@Override
	protected void initItem() {
		try {
			// EMPLOYEE_SERVICE_PUBLIC = new EmployeeServicePublicProxy();
			departmentServicePublic = new DepartmentServicePublicProxy();
			// memberServicePublic = new MemberServicePublicProxy();
			member = getAccount().getMember();
			departmentSearchs = new ArrayList<Department>();
			if (getAccount().isAdmin()) {
				Department[] deps = departmentServicePublic.findAll();
				for (int i = 0; i < deps.length; i++) {
					if (deps[i].getLevelDep() != null)
						if (deps[i].getLevelDep().getLevel() > 1)
							departmentSearchs.add(deps[i]);
				}

			} else {
				departmentSearchs.add(member.getDepartment());
			}
			if (departmentSearchs.size() != 0) {
				departmentSearchs = DepartmentUtil.sort(departmentSearchs);
				departmentSelected = departmentSearchs.get(0);
			}
			yearSearch = Calendar.getInstance().get(Calendar.YEAR);
			searchItem();
			allCourse = COURSE_SERVICE.findAll();
			// tim khoa hoc sap dien ra

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleComingCourses(List<Course> allCourse) {
		// Date currentDate = new Date();
		// for(Course c : allCourse){
		// if(currentDate.before(c.getStart_date())){
		// planDetailsComing.add(e)
		// }
		// }
	}

	public void planShowEdit() {
		// tim vi tri chuc danh theo nhan vien duoc chon
		positionsByEmpl = PositionJobDataService.vttheonhanvien(planSelected.getEmployee_code());
		positionsByEmplList = Arrays.asList(positionsByEmpl);
		// query danh sach chi tiet ke hoach
		detailsByPlan = PLAN_DETAIL_SERVICE.findByPlan(planSelected.getId());
		positionSelected = new PositionJobData();
		coursesByPosition = new ArrayList<>();
	}

	public void searchItem() {
		plansByDepart = PLAN_SERVICE.findByEmplCodeAndYear(member.getCode(), yearSearch);
		if (!plansByDepart.isEmpty()) {
			// tim vi tri chuc danh theo nhan vien duoc chon
			positionsByEmpl = PositionJobDataService.vttheonhanvien(plansByDepart.get(0).getEmployee_code());
			positionsByEmplList = Arrays.asList(positionsByEmpl);
			// query danh sach chi tiet ke hoach
			detailsByPlan = PLAN_DETAIL_SERVICE.findByPlan(plansByDepart.get(0).getId());
			positionSelected = new PositionJobData();
			coursesByPosition = new ArrayList<>();
		}
		// ke hoach chua duoc tao
		else {
			detailsByPlan = new ArrayList<>();
		}
	}

	public void ajaxHandleEndDate(Course item) {
		// System.out.println(item);
		// tim kiem va set ngay ket thuc
		for (int i = 0; i < allCourse.size(); i++) {
			if (allCourse.get(i).getId() == item.getId()) {
				// tinh ngay ket thuc
				allCourse.get(i).setEnd_date(DateUtil.addDays(item.getStart_date(), item.getTime()));
				break;
			}
		}
	}

	// tim danh sach khoa hoc theo vi tri
	public void positionOnRowSelect() {
		List<CoursePositionJob> coursePosi = COURSE_POSITION_JOB_SERVICE.findByPosition(positionSelected.getCode());
		List<Long> ids = new ArrayList<>();
		for (CoursePositionJob c : coursePosi) {
			ids.add(c.getCourse().getId());
		}
		coursesByPosition = COURSE_SERVICE.findByListId(ids);
		// tim khoa hoc nao da co thi set select = true
		for (int i = 0; i < coursesByPosition.size(); i++) {
			PlanDetail p = PLAN_DETAIL_SERVICE.findByCourseAndPlan(coursesByPosition.get(i).getId(),
					planSelected.getId());
			if (p != null && p.getId() != null) {
				coursesByPosition.get(i).setSelect(true);
			}
		}
	}

	public void planDetailOnRowSelect() {
		try {
			FacesContext fContext = FacesContext.getCurrentInstance();
			ExternalContext extContext = fContext.getExternalContext();
			HttpServletRequest ht = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			String path = "http://" + ht.getServerName() + ":" + ht.getServerPort()
					+ "/elearning/pages/nhanvien/chitietkhoahoc.htm?pdid=" + planDetailSelected.getId();
			extContext.redirect(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveOrUpdateTab3() {
		if (detailsByPlan != null) {
			for (int i = 0; i < detailsByPlan.size(); i++) {
				try {
					PLAN_DETAIL_SERVICE.update(detailsByPlan.get(i));
				} catch (Exception e) {
					e.printStackTrace();
					MessageView.ERROR("Lỗi");
					return;
				}
			}
			MessageView.INFO("Thành công");
			return;
		} else {
			MessageView.ERROR("Vui lòng thực hiện đúng trình tự");
			return;
		}
	}

	public void create() {
		try {
			// toan bo nhan vien nhom khac
			EmployeeDTO[] allMemberTemp;
			List<String> depList = new ArrayList<>();
			String[] depArray = null;
			// Phong ban
			depList.add(departmentSelected.getCode());
			depArray = depList.toArray(new String[depList.size()]);
			EMPLOYEE_SERVICE_PUBLIC = new EmployeeServicePublicProxy();
			allMemberTemp = EMPLOYEE_SERVICE_PUBLIC.findByDep(depArray);
			if (allMemberTemp != null) {
				// tat ca nhan vien
				List<EmployeeDTO> allMember = Arrays.asList(allMemberTemp);
				for (int i = 0; i < allMember.size(); i++) {
					Plan pNew = new Plan();
					pNew.setCreatedDate(new Date());
					pNew.setCreatedUser(member.getCode());
					pNew.setYear(yearSearch);
					pNew.setEmployee_code(allMember.get(i).getCode());
					pNew.setEmployee_name(allMember.get(i).getName());
					pNew.setDepartment_code(allMember.get(i).getCodeDepart());
					pNew.setDepartment_name(allMember.get(i).getNameDepart());
					// tim thu nhan vien do duoc tao chua
					Plan pEixst = PLAN_SERVICE.findByEmployeeCode(allMember.get(i).getCode());
					if (pEixst.getId() == null) {
						PLAN_SERVICE.create(pNew);
					}
				}
				plansByDepart = PLAN_SERVICE.findByDepartAndYear(departmentSelected.getCode(), yearSearch);
				MessageView.INFO("Thành công");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageView.ERROR("Lỗi");
		}
	}

	public void registerNewCourse() {
		try {
			if (plansByDepart != null && !plansByDepart.isEmpty()) {
				boolean coursesSelected = false;
				for (int i = 0; i < allCourse.size(); i++) {
					if (allCourse.get(i).isSelect()) {
						coursesSelected = true;
						PlanDetail pd = PLAN_DETAIL_SERVICE.findByCourseAndPlan(allCourse.get(i).getId(),
								plansByDepart.get(0).getId());
						// khoa hoc chua duoc tao
						// them moi
						if (pd.getId() == null) {
							// phai co ngay bat dau cho khoa hoc
							if (allCourse.get(i).getStart_date() != null && allCourse.get(i).getEnd_date() != null) {
								pd = new PlanDetail(allCourse.get(i), plansByDepart.get(0),
										allCourse.get(i).getStart_date(), allCourse.get(i).getEnd_date());
								pd = PLAN_DETAIL_SERVICE.create(pd);
								// tao toan bo ki nang theo khoa hoc
								List<Skill> skillsByCourseTemp = SKILL_SERVICE.findByCourse(allCourse.get(i).getId());
								for (Skill s : skillsByCourseTemp) {
									PlanDetailSkill pds = new PlanDetailSkill();
									pds.setSkill(s);
									pds.setPlan_detail(pd);
									pds.setCreatedDate(new Date());
									pds.setCreatedUser(member.getCode());
									PLAN_DETAIL_SKILL_SERVICE.create(pds);
								}
							}
							// khong co ngay bat dau
							else {
								MessageView.ERROR("Vui lòng nhập ngày bắt đầu khóa học");
								return;
							}
						}
						allCourse.get(i).setSelect(false);
					}
				}
				if (!coursesSelected) {
					MessageView.WARN("Vui lòng tích chọn khóa học xong nhấn đăng ký");
					return;
				}
				// query danh sach chi tiet ke hoach
				detailsByPlan = PLAN_DETAIL_SERVICE.findByPlan(plansByDepart.get(0).getId());
				MessageView.INFO("Thành công");
			}
			// ke hoach chua duoc tao
			else {
				MessageView.WARN("Kế hoạch năm chưa được tạo");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageView.ERROR("Lỗi");
		}
	}

	public void saveOrUpdate() {
		for (int i = 0; i < coursesByPosition.size(); i++) {
			if (coursesByPosition.get(i).isSelect()) {
				PlanDetail pdNew = new PlanDetail();
				pdNew.setPlan(planSelected);
				pdNew.setCourse(coursesByPosition.get(i));
				try {
					// kiem tra khoa hoc da ton tai chua
					PlanDetail pdCheck = PLAN_DETAIL_SERVICE.findByCourseAndPlan(coursesByPosition.get(i).getId(),
							planSelected.getId());
					if (pdCheck.getId() == null) {
						PLAN_DETAIL_SERVICE.create(pdNew);
					}
				} catch (Exception e) {
					e.printStackTrace();
					MessageView.ERROR("Lỗi");
					return;
				}
			}
		}
		detailsByPlan = PLAN_DETAIL_SERVICE.findByPlan(planSelected.getId());
		MessageView.INFO("Thành công");
	}

	public String moveToChiTietKhoaHoc(long itemId) {
		return "chitietkhoahoc?faces-redirect=true&pdid=" + itemId;
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

	public int getYearSearch() {
		return yearSearch;
	}

	public void setYearSearch(int yearSearch) {
		this.yearSearch = yearSearch;
	}

	public Department getDepartmentSelected() {
		return departmentSelected;
	}

	public void setDepartmentSelected(Department departmentSelected) {
		this.departmentSelected = departmentSelected;
	}

	public List<Department> getDepartmentSearchs() {
		return departmentSearchs;
	}

	public void setDepartmentSearchs(List<Department> departmentSearchs) {
		this.departmentSearchs = departmentSearchs;
	}

	public List<Plan> getPlansByDepart() {
		return plansByDepart;
	}

	public void setPlansByDepart(List<Plan> plansByDepart) {
		this.plansByDepart = plansByDepart;
	}

	public List<Plan> getPlansFilter() {
		return plansFilter;
	}

	public void setPlansFilter(List<Plan> plansFilter) {
		this.plansFilter = plansFilter;
	}

	public Plan getPlanSelected() {
		return planSelected;
	}

	public void setPlanSelected(Plan planSelected) {
		this.planSelected = planSelected;
	}

	public List<PositionJobData> getPositionsByEmplList() {
		return positionsByEmplList;
	}

	public void setPositionsByEmplList(List<PositionJobData> positionsByEmplList) {
		this.positionsByEmplList = positionsByEmplList;
	}

	public PositionJobData getPositionSelected() {
		return positionSelected;
	}

	public void setPositionSelected(PositionJobData positionSelected) {
		this.positionSelected = positionSelected;
	}

	public List<Course> getCoursesByPosition() {
		return coursesByPosition;
	}

	public void setCoursesByPosition(List<Course> coursesByPosition) {
		this.coursesByPosition = coursesByPosition;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List<PlanDetail> getDetailsByPlan() {
		return detailsByPlan;
	}

	public void setDetailsByPlan(List<PlanDetail> detailsByPlan) {
		this.detailsByPlan = detailsByPlan;
	}

	public PlanDetail getPlanDetailSelected() {
		return planDetailSelected;
	}

	public void setPlanDetailSelected(PlanDetail planDetailSelected) {
		this.planDetailSelected = planDetailSelected;
	}

	public List<Course> getAllCourse() {
		return allCourse;
	}

	public void setAllCourse(List<Course> allCourse) {
		this.allCourse = allCourse;
	}

	public List<PlanDetail> getPlanDetailsComing() {
		return planDetailsComing;
	}

	public void setPlanDetailsComing(List<PlanDetail> planDetailsComing) {
		this.planDetailsComing = planDetailsComing;
	}
}
