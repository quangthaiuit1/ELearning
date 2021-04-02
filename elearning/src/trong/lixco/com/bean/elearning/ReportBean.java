package trong.lixco.com.bean.elearning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

import trong.lixco.com.account.servicepublics.Department;
import trong.lixco.com.account.servicepublics.DepartmentServicePublic;
import trong.lixco.com.account.servicepublics.DepartmentServicePublicProxy;
import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.bean.entities.DepartmentData;
import trong.lixco.com.bean.entities.DepartmentDataService;
import trong.lixco.com.bean.entities.EmployeeData;
import trong.lixco.com.bean.entities.EmployeeDataService;
import trong.lixco.com.bean.entities.TimekeepingData;
import trong.lixco.com.bean.entities.TimekeepingDataService;
import trong.lixco.com.bean.staticentity.DateUtil;
import trong.lixco.com.ejb.service.elearning.InfoKeyValueService;
import trong.lixco.com.ejb.service.elearning.PlanDetailService;
import trong.lixco.com.ejb.service.elearning.PlanDetailSkillService;
import trong.lixco.com.ejb.service.elearning.PlanService;
import trong.lixco.com.jpa.entities.Course;
import trong.lixco.com.jpa.entities.InfoKeyValue;
import trong.lixco.com.jpa.entities.Plan;
import trong.lixco.com.jpa.entities.PlanDetail;
import trong.lixco.com.jpa.entities.PlanDetailSkill;
import trong.lixco.com.servicepublic.EmployeeDTO;
import trong.lixco.com.servicepublic.EmployeeServicePublic;
import trong.lixco.com.servicepublic.EmployeeServicePublicProxy;
import trong.lixco.com.util.DepartmentUtil;

@Named
@ViewScoped
public class ReportBean extends AbstractBean<Course> {
	private static final long serialVersionUID = 1L;
	private Department departmentSearch;
	private List<Department> departmentSearchs;
	private List<Department> departmentsSelected;// findAllNew

	boolean isDisable = false;
	SimpleDateFormat formatter;
	private String loaiBaoCao;
	private Date dateSearch;
	// bao cao chinh xac
	private Date dateSearchExactly;

	private Date fromDate;
	private Date toDate;

	private boolean[] checkedRenderView;
	private int valueChecked;

	private List<Plan> plansByDate;
	private List<PlanDetail> pdsByDate;
	private PlanDetail pdSelected;
	private List<PlanDetailSkill> pdskillsByPD;
	private List<PlanDetailSkill> pdskillsFilterValue;
	private int tongso = 0;// tong so hoc vien giam gia theo tung khoa
	private int sldat = 0;
	private int slkhongdat = 0;
	private int valueLoaiBaoCao = 0;

	private Member member;
	EmployeeServicePublic EMPLOYEE_SERVICE_PUBLIC;
	DepartmentServicePublic DEPARTMENT_SERVICE_PUBLIC;
	@Inject
	private PlanService PLAN_SERVICE;
	@Inject
	private PlanDetailService PLAN_DETAIL_SERVICE;
	@Inject
	private PlanDetailSkillService PLAN_DETAIL_SKILL_SERVICE;
	@Inject
	private InfoKeyValueService INFO_KEY_VALUE_SERVICE;

	@Override
	protected void initItem() {
		try {
			DEPARTMENT_SERVICE_PUBLIC = new DepartmentServicePublicProxy();
			EMPLOYEE_SERVICE_PUBLIC = new EmployeeServicePublicProxy();
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			dateSearch = new Date();
			// handle shifts
			// du bao suat an
			Date currentDate00 = new Date();
			// currentDate00 = DateUtil.SET_HHMMSS_00(currentDate00);
			currentDate00 = DateUtil.DATE_WITHOUT_TIME(currentDate00);
			fromDate = currentDate00;
			toDate = currentDate00;
			pdSelected = new PlanDetail();

			dateSearchExactly = currentDate00;
			checkedRenderView = new boolean[2];
			for (int i = 0; i < checkedRenderView.length; i++) {
				checkedRenderView[i] = false;
			}
			member = getAccount().getMember();
			// code moi cho tim theo phong ban
			departmentSearchs = new ArrayList<Department>();
			departmentsSelected = new ArrayList<>();
			Department[] deps = DEPARTMENT_SERVICE_PUBLIC.findAll();
			for (int i = 0; i < deps.length; i++) {
				if (deps[i].getLevelDep() != null)
					if (deps[i].getLevelDep().getLevel() > 1)
						departmentSearchs.add(deps[i]);
			}
			if (departmentSearchs.size() != 0) {
				departmentSearchs = DepartmentUtil.sort(departmentSearchs);
				departmentSearch = departmentSearchs.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleRenderView() {
		// set false toan bo view
		for (int i = 0; i < checkedRenderView.length; i++) {
			checkedRenderView[i] = false;
		}
		if (valueChecked != 0) {
			// thay i < tai day
			for (int i = 1; i < 2; i++) {
				if (valueChecked == i) {
					checkedRenderView[i - 1] = true;
				} else {
					checkedRenderView[i - 1] = false;
				}
			}
			PrimeFaces.current().ajax().update("formBaoCao");
		} else {
			// set false toan bo view
			for (int i = 0; i < checkedRenderView.length; i++) {
				checkedRenderView[i] = false;
			}
			PrimeFaces.current().ajax().update("formBaoCao");
			return;
		}
	}

	public void searchFromDateToDate() {
		try {
			pdsByDate = PLAN_DETAIL_SERVICE.findByDateToDate(fromDate, toDate);
			// tim diem pass tu db
			InfoKeyValue scorePass = INFO_KEY_VALUE_SERVICE.findByName("pass_score");
			// tim khoa hoc dat - khong dat
			for (int i = 0; i < pdsByDate.size(); i++) {
				if (pdsByDate.get(i).getAvg_score() >= Integer.parseInt(scorePass.getValue())) {
					pdsByDate.get(i).setResultTemp("Đạt");
					sldat = sldat + 1;
				} else {
					pdsByDate.get(i).setResultTemp("Không đạt");
				}
			}
			tongso = pdsByDate.size();
			slkhongdat = tongso - sldat;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ajaxHandleTab1Selected() {
		pdskillsByPD = PLAN_DETAIL_SKILL_SERVICE.findBySkillAndPlanDetail(0, pdSelected.getId());
	}

	// bao cao danh sach nhan vien khong quet van tay (duoi nha an)
	public void baoCaoNVKhongQuetVanTayExcel() {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
			XSSFSheet sheet = null;
			List<PlanDetail> pdsTemp = new ArrayList<>();
			if (valueLoaiBaoCao == 0) {
				sheet = workbook.createSheet("ALL");
				pdsTemp = pdsByDate;
			}
			if (valueLoaiBaoCao == 1) {
				sheet = workbook.createSheet("Đạt");
				// tim diem pass tu db
				InfoKeyValue scorePass = INFO_KEY_VALUE_SERVICE.findByName("pass_score");
				for (PlanDetail p : pdsByDate) {
					if (p.getAvg_score() >= Integer.parseInt(scorePass.getValue())) {
						pdsTemp.add(p);
					}
				}
			}
			if (valueLoaiBaoCao == 2) {
				sheet = workbook.createSheet("Không đạt");
				// tim diem pass tu db
				InfoKeyValue scorePass = INFO_KEY_VALUE_SERVICE.findByName("pass_score");
				for (PlanDetail p : pdsByDate) {
					if (p.getAvg_score() < Integer.parseInt(scorePass.getValue())) {
						pdsTemp.add(p);
					}
				}
			}
			int rownum = 0;
			Cell cell;
			Row row;
			XSSFCellStyle style = createStyleForTitle(workbook);
			style.setAlignment(CellStyle.ALIGN_CENTER);

			// cell style for date
			XSSFCellStyle cellStyleDate = workbook.createCellStyle();
			CreationHelper createHelper = workbook.getCreationHelper();
			cellStyleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

			row = sheet.createRow(rownum);

			// EmpNo
			cell = row.createCell(0);
			cell.setCellValue("Mã NV");
			// xep loai// EmpName
			cell = row.createCell(1);
			cell.setCellValue("Tên NV");
			cell.setCellStyle(style);
			// Salary
			cell = row.createCell(2);
			cell.setCellValue("Phòng ban");
			cell.setCellStyle(style);

			cell = row.createCell(3);
			cell.setCellValue("Khóa học");
			cell.setCellStyle(style);

			cell = row.createCell(4);
			cell.setCellValue("Ngày bắt đầu");
			cell.setCellStyle(style);

			cell = row.createCell(5);
			cell.setCellValue("Ngày kết thúc");
			cell.setCellStyle(style);

			cell = row.createCell(6);
			cell.setCellValue("Điểm trung bình");
			cell.setCellStyle(style);

			// Data
			if (!pdsTemp.isEmpty()) {
				for (PlanDetail f : pdsTemp) {
					// Gson gson = new Gson();
					rownum++;
					row = sheet.createRow(rownum);

					// ma nhan vien
					cell = row.createCell(0);
					cell.setCellValue(f.getPlan().getEmployee_code());
					// ten nhan vien
					cell = row.createCell(1);
					cell.setCellValue(f.getPlan().getEmployee_name());

					// ten phong ban
					cell = row.createCell(2);
					DepartmentData departmentTemp = DepartmentDataService.timtheoma(f.getPlan().getDepartment_code());
					// kiem tra phong ban cap may
					// phong cap 2
					if (departmentTemp.getLevel() == 2) {
						cell.setCellValue(departmentTemp.getName());
					}
					if (departmentTemp.getLevel() == 3) {
						DepartmentData departmentLevel2 = DepartmentDataService
								.timtheoma(departmentTemp.getCodeDepart());
						if (departmentLevel2 != null) {
							if (departmentLevel2 != null) {
								if (departmentLevel2.getCode().equals("30005")
										|| departmentLevel2.getCode().equals("30015")
										|| departmentLevel2.getCode().equals("30018")) {
									cell.setCellValue(departmentTemp.getName());
								} else {
									cell.setCellValue(departmentLevel2.getName());
								}
							}
						}
					}

					// ten nhan vien
					cell = row.createCell(3);
					cell.setCellValue(f.getCourse().getName());

					// ngay
					cell = row.createCell(4);
					cell.setCellValue(f.getStart_time());
					cell.setCellStyle(cellStyleDate);

					cell = row.createCell(5);
					cell.setCellValue(f.getEnd_time());
					cell.setCellStyle(cellStyleDate);

					// ten nhan vien
					cell = row.createCell(6);
					cell.setCellValue(f.getAvg_score());
				}
			}

			String filename = "baocao.xlsx";
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			externalContext.setResponseContentType("application/vnd.ms-excel");
			externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			workbook.write(externalContext.getResponseOutputStream());
			facesContext.responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// tim danh sach toan bo nhan vien o HCM
	public List<EmployeeData> totalEmployeeHCM() {
		// Danh sach nhan vien o HCM khong bao gom phong kinh doanh
		// tao list phong ban loai bo
		String[] departmentCodeArraySub = new String[] { "40019", "40001", "40007", "40008", "40009", "40010", "40011",
				"40012", "40056", "40013", "30010", "40014", "40015", "40016", "40047", "40048", "40017", "40018",
				"40020", "40021", "40022" };
		List<String> departmentCodeArraySubArr = Arrays.asList(departmentCodeArraySub);
		DepartmentData[] departmentHCMArray = DepartmentDataService.timtheophongquanly("20002");
		List<DepartmentData> departmentHCM = new ArrayList<>(Arrays.asList(departmentHCMArray));
		// tao string danh sach ma phong ban
		StringBuilder departmentCodeString = new StringBuilder();
		for (int i = 0; i < departmentHCM.size(); i++) {
			boolean isSub = false;
			for (int j = 0; j < departmentCodeArraySubArr.size(); j++) {
				if (departmentHCM.get(i).getCode().equals(departmentCodeArraySubArr.get(j))) {
					isSub = true;
					break;
				}
			}
			if (!isSub) {
				departmentCodeString.append(departmentHCM.get(i).getCode());
				departmentCodeString.append(",");
			}
		}
		String departmentCodeStringNew = departmentCodeString.toString();
		if (departmentCodeStringNew.endsWith(",")) {
			departmentCodeStringNew = departmentCodeStringNew.substring(0, departmentCodeStringNew.length() - 1);
		}
		EmployeeData[] employees = EmployeeDataService.timtheodsphongban(departmentCodeStringNew);
		List<EmployeeData> employeesAfterFilterKhongAn = new ArrayList<>();
		// loc ra nhung nguoi khong an com truong
		for (EmployeeData e : employees) {
			// 0006768 la account nha an -> loc ra luon
			if (!e.isQuitEating() && !e.getCode().equals("0006768")) {
				employeesAfterFilterKhongAn.add(e);
			}
		}
		return employeesAfterFilterKhongAn;

	}
	// end bao cao danh sach nhan vien khong dang ky mon an

	private static XSSFCellStyle createStyleForTitle(XSSFWorkbook workbook) {
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		XSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		return style;
	}

	public Date getDateSearch() {
		return dateSearch;
	}

	public void setDateSearch(Date dateSearch) {
		this.dateSearch = dateSearch;
	}

	public String getLoaiBaoCao() {
		return loaiBaoCao;
	}

	public void setLoaiBaoCao(String loaiBaoCao) {
		this.loaiBaoCao = loaiBaoCao;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public boolean[] getCheckedRenderView() {
		return checkedRenderView;
	}

	public void setCheckedRenderView(boolean[] checkedRenderView) {
		this.checkedRenderView = checkedRenderView;
	}

	public int getValueChecked() {
		return valueChecked;
	}

	public void setValueChecked(int valueChecked) {
		this.valueChecked = valueChecked;
	}

	public Date getDateSearchExactly() {
		return dateSearchExactly;
	}

	public void setDateSearchExactly(Date dateSearchExactly) {
		this.dateSearchExactly = dateSearchExactly;
	}

	public boolean isDisable() {
		return isDisable;
	}

	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

	public Department getDepartmentSearch() {
		return departmentSearch;
	}

	public void setDepartmentSearch(Department departmentSearch) {
		this.departmentSearch = departmentSearch;
	}

	public List<Department> getDepartmentSearchs() {
		return departmentSearchs;
	}

	public void setDepartmentSearchs(List<Department> departmentSearchs) {
		this.departmentSearchs = departmentSearchs;
	}

	public List<Department> getDepartmentsSelected() {
		return departmentsSelected;
	}

	public void setDepartmentsSelected(List<Department> departmentsSelected) {
		this.departmentsSelected = departmentsSelected;
	}

	public List<PlanDetail> getPdsByDate() {
		return pdsByDate;
	}

	public void setPdsByDate(List<PlanDetail> pdsByDate) {
		this.pdsByDate = pdsByDate;
	}

	public PlanDetail getPdSelected() {
		return pdSelected;
	}

	public void setPdSelected(PlanDetail pdSelected) {
		this.pdSelected = pdSelected;
	}

	public List<PlanDetailSkill> getPdskillsByPD() {
		return pdskillsByPD;
	}

	public void setPdskillsByPD(List<PlanDetailSkill> pdskillsByPD) {
		this.pdskillsByPD = pdskillsByPD;
	}

	public int getTongso() {
		return tongso;
	}

	public void setTongso(int tongso) {
		this.tongso = tongso;
	}

	public int getSldat() {
		return sldat;
	}

	public void setSldat(int sldat) {
		this.sldat = sldat;
	}

	public int getSlkhongdat() {
		return slkhongdat;
	}

	public void setSlkhongdat(int slkhongdat) {
		this.slkhongdat = slkhongdat;
	}

	public List<PlanDetailSkill> getPdskillsFilterValue() {
		return pdskillsFilterValue;
	}

	public void setPdskillsFilterValue(List<PlanDetailSkill> pdskillsFilterValue) {
		this.pdskillsFilterValue = pdskillsFilterValue;
	}

	public int getValueLoaiBaoCao() {
		return valueLoaiBaoCao;
	}

	public void setValueLoaiBaoCao(int valueLoaiBaoCao) {
		this.valueLoaiBaoCao = valueLoaiBaoCao;
	}

	@Override
	protected Logger getLogger() {
		return null;
	}
}
