package trong.lixco.com.bean.elearning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

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
import trong.lixco.com.bean.staticentity.MessageView;
import trong.lixco.com.ejb.service.elearning.CoursePositionJobService;
import trong.lixco.com.ejb.service.elearning.CourseService;
import trong.lixco.com.ejb.service.elearning.PlanDetailService;
import trong.lixco.com.ejb.service.elearning.PlanService;
import trong.lixco.com.jpa.entities.Course;
import trong.lixco.com.jpa.entities.CoursePositionJob;
import trong.lixco.com.jpa.entities.Plan;
import trong.lixco.com.jpa.entities.PlanDetail;
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
	private List<PlanDetail> detailsByPlan;

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

	@Override
	protected void initItem() {
		try {
			EMPLOYEE_SERVICE_PUBLIC = new EmployeeServicePublicProxy();
			departmentServicePublic = new DepartmentServicePublicProxy();
			memberServicePublic = new MemberServicePublicProxy();
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
			searchItem();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			List<EmployeeDTO> allMemberOther = new ArrayList<>();
			EmployeeDTO[] allMemberTemp;
			List<String> depList = new ArrayList<>();
			String[] depArray = null;
			// Phong ban
			depList.add(departmentSelected.getCode());
			depArray = depList.toArray(new String[depList.size()]);
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
				plansByDepart = PLAN_SERVICE.findByDepart(departmentSelected.getCode());
				MessageView.INFO("Thành công");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageView.ERROR("Lỗi");
		}
	}

	public void removeKPIPersonOther() {

	}

	public void delete() {

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

}