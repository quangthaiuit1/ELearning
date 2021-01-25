package trong.lixco.com.bean.elearning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import com.ibm.icu.text.SimpleDateFormat;

import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.bean.staticentity.MessageView;
import trong.lixco.com.ejb.service.elearning.CoursePositionJobService;
import trong.lixco.com.ejb.service.elearning.CourseService;
import trong.lixco.com.ejb.service.elearning.PositionJobService;
import trong.lixco.com.jpa.entities.Course;
import trong.lixco.com.jpa.entities.CoursePositionJob;
import trong.lixco.com.jpa.entities.PositionJob;

@Named
@ViewScoped
public class CoursePositionJobBean extends AbstractBean<CoursePositionJob> {

	private static final long serialVersionUID = 1L;
	private List<PositionJob> positions;
	private List<PositionJob> positionsFilter;
	private List<PositionJob> positionsSettingFilter;
	private PositionJob positionSelected;
	// private PositionJob positionSettingSelected;
	private List<CoursePositionJob> coursePositionByPosition;
	private List<Course> coursesByPosition;
	private List<Course> allCourse;
	// private List<Course> coursesSelected;
	@Inject
	private PositionJobService POSITION_JOB_SERVICE;
	@Inject
	private CoursePositionJobService COURSE_POSITION_JOB_SERVICE;
	@Inject
	private CourseService COURSE_SERVICE;

	@Override
	protected void initItem() {
		positions = POSITION_JOB_SERVICE.findAll();
		coursesByPosition = new ArrayList<>();
		allCourse = COURSE_SERVICE.findAll();
	}

	public void positionJobOnRowSelected() {
		coursesByPosition = new ArrayList<>();
		coursePositionByPosition = COURSE_POSITION_JOB_SERVICE.findByPosition(positionSelected.getCode());
		if (!coursePositionByPosition.isEmpty()) {
			for (CoursePositionJob c : coursePositionByPosition) {
				coursesByPosition.add(c.getCourse());
			}
		}
	}

	// public void positionJobSettingOnRowSelected() {
	// allCourse = COURSE_SERVICE.findAll();
	// coursesByPosition = new ArrayList<>();
	// coursePositionByPosition =
	// COURSE_POSITION_JOB_SERVICE.findByPosition(positionSettingSelected.getCode());
	// if (!coursePositionByPosition.isEmpty()) {
	// for (CoursePositionJob c : coursePositionByPosition) {
	// coursesByPosition.add(c.getCourse());
	// }
	// }
	// for (int i = 0; i < allCourse.size(); i++) {
	// boolean isExist = false;
	// for (int j = 0; j < coursesByPosition.size(); j++) {
	// if (allCourse.get(i).getId() == coursesByPosition.get(j).getId()) {
	// isExist = true;
	// break;
	// }
	// }
	// if (isExist) {
	// allCourse.get(i).setSelect(true);
	// }
	// }
	// }

	public void saveOrUpdateBackup() {
		// if (positionSettingSelected == null) {
		// MessageView.WARN("Không có thay đổi");
		// return;
		// }
		// coursesSelected = new ArrayList<>();
		// for (int i = 0; i < allCourse.size(); i++) {
		// if (allCourse.get(i).isSelect()) {
		// coursesSelected.add(allCourse.get(i));
		// // allCourse.get(i).setSelect(false);
		// }
		// }
		// // kiem tra vi tri do da duoc tao chua
		// List<CoursePositionJob> coursesIsExist = COURSE_POSITION_JOB_SERVICE
		// .findByPosition(positionSettingSelected.getCode());
		// if (coursesIsExist.isEmpty()) {
		// // tao moi toan bo
		// boolean isSuccess = true;
		// for (int i = 0; i < coursesSelected.size(); i++) {
		// // 1.position job , 2.course
		// CoursePositionJob cpNew = new
		// CoursePositionJob(positionSettingSelected.getCode(),
		// coursesSelected.get(i));
		// cpNew.setCreatedDate(new Date());
		// cpNew.setCreatedUser(getAccount().getMember().getCode());
		// try {
		// cpNew = COURSE_POSITION_JOB_SERVICE.create(cpNew);
		// } catch (Exception e) {
		// isSuccess = false;
		// e.printStackTrace();
		// }
		// }
		// if (isSuccess) {
		// MessageView.INFO("Thành công");
		// } else {
		// MessageView.ERROR("Lỗi");
		// }
		// }
		// // vi tri da duoc tao -> them vi tri khoa hoc moi
		// else {
		// for (int i = 0; i < coursesIsExist.size(); i++) {
		// boolean exist = false;
		// for (int j = 0; j < coursesSelected.size(); j++) {
		// if (coursesIsExist.get(i).getCourse().getId() ==
		// coursesSelected.get(j).getId()) {
		// exist = true;
		// break;
		// }
		// }
		// if (!exist) {
		// try {
		// COURSE_POSITION_JOB_SERVICE.delete(coursesIsExist.get(i));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// // list vi tri khoa hoc sau khi xoa
		// coursesIsExist =
		// COURSE_POSITION_JOB_SERVICE.findByPosition(positionSettingSelected.getCode());
		// for (int i = 0; i < coursesSelected.size(); i++) {
		// boolean exist = false;
		// for (int j = 0; j < coursesIsExist.size(); j++) {
		// if (coursesSelected.get(i).getId() ==
		// coursesIsExist.get(j).getCourse().getId()) {
		// exist = true;
		// }
		// }
		// if (!exist) {
		// // 1.position job , 2.course
		// CoursePositionJob cpNew = new
		// CoursePositionJob(positionSettingSelected.getCode(),
		// coursesSelected.get(i));
		// cpNew.setCreatedDate(new Date());
		// cpNew.setCreatedUser(getAccount().getMember().getCode());
		// try {
		// cpNew = COURSE_POSITION_JOB_SERVICE.create(cpNew);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// MessageView.INFO("Thành công");
		// }
	}

	public void saveOrUpdate() {
		// loc ra danh sach khoa hoc duoc chon
		List<Course> coursesSelected = new ArrayList<>();
		for (int j = 0; j < allCourse.size(); j++) {
			if (allCourse.get(j).isSelect()) {
				coursesSelected.add(allCourse.get(j));
			}
		}
		if (coursesSelected.isEmpty()) {
			MessageView.WARN("Không có khóa học nào được chọn");
			return;
		}
		boolean isSuccess = true;
		boolean isSelected = false; // kiem tra co vi tri nao duoc chon hay
									// khong
		for (int i = 0; i < positions.size(); i++) {
			if (positions.get(i).isSelect()) {
				isSelected = true;
				// kiem tra vi tri do da duoc tao chua
				List<CoursePositionJob> coursesIsExist = COURSE_POSITION_JOB_SERVICE
						.findByPosition(positions.get(i).getCode());
				if (coursesIsExist.isEmpty()) {
					// tao moi toan bo
					for (int j = 0; j < coursesSelected.size(); j++) {
						// 1.position job , 2.course
						CoursePositionJob cpNew = new CoursePositionJob(positions.get(i).getCode(),
								coursesSelected.get(j));
						cpNew.setCreatedDate(new Date());
						cpNew.setCreatedUser(getAccount().getMember().getCode());
						try {
							cpNew = COURSE_POSITION_JOB_SERVICE.create(cpNew);
						} catch (Exception e) {
							isSuccess = false;
							e.printStackTrace();
						}
					}
				}
				// vi tri da duoc tao -> them vi tri khoa hoc moi
				else {
					for (int j = 0; j < coursesIsExist.size(); j++) {
						boolean exist = false;
						for (int k = 0; k < coursesSelected.size(); k++) {
							if (coursesIsExist.get(j).getCourse().getId() == coursesSelected.get(k).getId()) {
								exist = true;
								break;
							}
						}
						if (!exist) {
							try {
								COURSE_POSITION_JOB_SERVICE.delete(coursesIsExist.get(j));
							} catch (Exception e) {
								isSuccess = false;
								e.printStackTrace();
							}
						}
					}
					// list vi tri khoa hoc sau khi xoa
					coursesIsExist = COURSE_POSITION_JOB_SERVICE.findByPosition(positions.get(i).getCode());
					for (int j = 0; j < coursesSelected.size(); j++) {
						boolean exist = false;
						for (int k = 0; k < coursesIsExist.size(); k++) {
							if (coursesSelected.get(j).getId() == coursesIsExist.get(k).getCourse().getId()) {
								exist = true;
							}
						}
						if (!exist) {
							// 1.position job , 2.course
							CoursePositionJob cpNew = new CoursePositionJob(positions.get(i).getCode(),
									coursesSelected.get(j));
							cpNew.setCreatedDate(new Date());
							cpNew.setCreatedUser(getAccount().getMember().getCode());
							try {
								cpNew = COURSE_POSITION_JOB_SERVICE.create(cpNew);
							} catch (Exception e) {
								isSuccess = false;
								e.printStackTrace();
							}
						}
					}
				}
			}

		}
		if (!isSelected) {
			MessageView.WARN("Không có vị trí/chức danh nào được chọn");
			return;
		}
		if (isSuccess) {
			MessageView.INFO("Thành công");
		} else {
			MessageView.ERROR("Lỗi");
		}
	}

	public void fileKhoaHocViTriMau() {
		try {
			PrimeFaces.current().executeScript("target='_blank';monitorDownload( showStatus , hideStatus)");
			String filename = "ViTri_KhoaHoc";
			HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance()
					.getExternalContext().getResponse();
			httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + filename + ".xlsx");
			ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
			String file = FacesContext.getCurrentInstance().getExternalContext()
					.getRealPath("/resources/maufile/ViTri_KhoaHoc_dlmau.xlsx");
			InputStream inputStream = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) != -1) {
				servletOutputStream.write(buffer, 0, len);
			}
			FacesContext.getCurrentInstance().responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// import file excel
	public void handleFileUpload(FileUploadEvent event) throws IOException {

		// context.execute("PF('dlg1').show();");
		long size = event.getFile().getSize();

		String filename = FilenameUtils.getBaseName(event.getFile().getFileName());

		String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/temp/");
		File checkFile = new File(path);
		if (!checkFile.exists()) {
			checkFile.mkdirs();
		}

		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
		String name = filename + fmt.format(new Date())
				+ event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf('.'));
		File file = new File(path + "/" + name);

		InputStream is = event.getFile().getInputstream();
		OutputStream out = new FileOutputStream(file);
		byte buf[] = new byte[(int) size];
		int len;
		while ((len = is.read(buf)) > 0)
			out.write(buf, 0, len);
		is.close();
		out.close();

		InputStream inp = null;
		try {
			inp = new FileInputStream(file.getAbsolutePath());
			Workbook wb = WorkbookFactory.create(inp);

			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				System.out.println(wb.getSheetAt(i).getSheetName());
				echoAsCSVFile(wb.getSheetAt(i));
			}
		} catch (Exception ex) {
		} finally {
			try {
				inp.close();
			} catch (IOException ex) {
			}
		}
	}

	public void echoAsCSVFile(Sheet sheet) {
		Row row = null;
		boolean isError = false;

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			try {
				int codePJob = (int) Double.parseDouble(row.getCell(0).toString());
				int courseId = (int) Double.parseDouble(row.getCell(1).toString());
				// //Luu doi tuong xuong DB
				CoursePositionJob cpCreate = new CoursePositionJob();
				cpCreate.setPosition_job_code(Integer.toString(codePJob));
				Course courseExist = COURSE_SERVICE.findById(courseId);
				// truong hop sai id khoa hoc -> khong ton tai
				if (courseExist == null) {
					MessageView.ERROR("Lỗi. Không thể import");
					return;
				}
				cpCreate.setCourse(courseExist);
				cpCreate.setCreatedDate(new Date());
				cpCreate.setCreatedUser(getAccount().getMember().getCode());
				try {
					COURSE_POSITION_JOB_SERVICE.create(cpCreate);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				isError = true;
				e.printStackTrace();
			}
		}
		if (isError) {
			MessageView.ERROR("Lỗi!");
		} else {
			MessageView.INFO("Thành công");
		}
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

	public List<PositionJob> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionJob> positions) {
		this.positions = positions;
	}

	public List<PositionJob> getPositionsFilter() {
		return positionsFilter;
	}

	public void setPositionsFilter(List<PositionJob> positionsFilter) {
		this.positionsFilter = positionsFilter;
	}

	public PositionJob getPositionSelected() {
		return positionSelected;
	}

	public void setPositionSelected(PositionJob positionSelected) {
		this.positionSelected = positionSelected;
	}

	public List<Course> getCoursesByPosition() {
		return coursesByPosition;
	}

	public void setCoursesByPosition(List<Course> coursesByPosition) {
		this.coursesByPosition = coursesByPosition;
	}

	public List<Course> getAllCourse() {
		return allCourse;
	}

	public void setAllCourse(List<Course> allCourse) {
		this.allCourse = allCourse;
	}

	// public PositionJob getPositionSettingSelected() {
	// return positionSettingSelected;
	// }
	//
	// public void setPositionSettingSelected(PositionJob
	// positionSettingSelected) {
	// this.positionSettingSelected = positionSettingSelected;
	// }

	public List<PositionJob> getPositionsSettingFilter() {
		return positionsSettingFilter;
	}

	public void setPositionsSettingFilter(List<PositionJob> positionsSettingFilter) {
		this.positionsSettingFilter = positionsSettingFilter;
	}

}
