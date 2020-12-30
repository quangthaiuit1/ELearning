package trong.lixco.com.bean.elearning;

import java.io.File;
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

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.bean.staticentity.MessageView;
import trong.lixco.com.bean.staticentity.Notification;
import trong.lixco.com.ejb.service.elearning.CourseTypeService;
import trong.lixco.com.jpa.entities.CourseType;
import trong.lixco.com.util.Notify;

@Named
@ViewScoped
public class CourseTypeBean extends AbstractBean<CourseType> {

	private static final long serialVersionUID = 1L;
	private List<CourseType> allCourseType;
	private List<CourseType> courseTypesFilter;
	private CourseType courseTypeNew;
	private CourseType courseTypeUpdate;

	private Notify notify;
	private Member member;
	// Thu muc chua image
	private String destination = "D:\\WebTimViec\\WebTimViec_WEB\\WebContent\\resources\\images\\";

	@Inject
	private CourseTypeService COURSE_TYPE_SERVICE;

	@Override
	protected void initItem() {
		courseTypeNew = new CourseType();
		courseTypeUpdate = new CourseType();
		notify = new Notify(FacesContext.getCurrentInstance());
		allCourseType = new ArrayList<CourseType>();
		// categoryFoodsRemove = new ArrayList<>();
		try {
			allCourseType = COURSE_TYPE_SERVICE.findAllNew();
		} catch (Exception e) {
			e.printStackTrace();
		}
		member = getAccount().getMember();

	}

	// public void addRowNew() {
	// CategoryFood categoryTemp = new CategoryFood();
	// categoryTemp.setCreatedUser(member.getName());
	// categoryFoods.add(categoryTemp);
	// }

	// public void saveOrUpdate() {
	// // remove list deleted
	// for (CategoryFood c : categoryFoodsRemove) {
	// CATEGORY_FOOD_SERVICE.delete(c);
	// }
	//
	// // update
	// for (int i = 0; i < categoryFoods.size(); i++) {
	// // chua co duoi db
	// if (categoryFoods.get(i).getId() == null) {
	// if (!categoryFoods.get(i).getName().isEmpty() &&
	// categoryFoods.get(i).getName() != null) {
	// CategoryFood a = CATEGORY_FOOD_SERVICE.create(categoryFoods.get(i));
	// if (a != null) {
	// System.out.println("Thanh cong");
	// } else {
	// System.out.println("That bai");
	// }
	// }
	// } else {
	// CATEGORY_FOOD_SERVICE.update(categoryFoods.get(i));
	// }
	// }
	// categoryFoodsRemove = new ArrayList<>();
	// CommonService.successNotify();
	// }

	public void deleteRow(CourseType item) {
		for (CourseType course : allCourseType) {
			if (course.getId() == item.getId()) {
				try {
					boolean delete = COURSE_TYPE_SERVICE.delete(item);
					if (delete) {
						allCourseType = COURSE_TYPE_SERVICE.findAllNew();
						Notification.NOTI_SUCCESS("Xóa thành công");
					} else {
						Notification.NOTI_ERROR("Không thể xóa");
					}
				} catch (Exception e) {
					Notification.NOTI_ERROR("Không thể xóa");
					e.printStackTrace();
				}
			}
		}
	}

	// NEW update category food
	public void updateItem() {
		try {
			// check co trung ten hay khong
			List<CourseType> checkExistName = COURSE_TYPE_SERVICE.findByName(courseTypeUpdate.getName());
			boolean isExist = false;
			if (checkExistName.size() < 2) {
				if (checkExistName.size() == 1) {
					if (checkExistName.get(0).getId() != courseTypeUpdate.getId()) {
						isExist = true;
					}
				}
				// ten chua co
				if (!isExist) {
					courseTypeUpdate.setModifiedDate(new Date());
					courseTypeUpdate.setModifiedUser(member.getCode());
					// truong hop khong co phan tu nao trung -> cap nhat ten
					CourseType cfUpdate = COURSE_TYPE_SERVICE.update(courseTypeUpdate);
					if (cfUpdate != null) {
						// Notification.NOTI_SUCCESS("Thành công");
						MessageView.INFO("Thành công");
						allCourseType = COURSE_TYPE_SERVICE.findAllNew();
						courseTypeUpdate = new CourseType();
						PrimeFaces current = PrimeFaces.current();
						current.executeScript("PF('widgetCapNhatMonAn').hide();");
					} else {
						Notification.NOTI_ERROR("Thất bại");
					}
				} else {
					allCourseType = COURSE_TYPE_SERVICE.findAllNew();
					Notification.NOTI_ERROR("Tên món ăn đã tồn tại");
				}
			} else {
				allCourseType = COURSE_TYPE_SERVICE.findAllNew();
				Notification.NOTI_ERROR("Tên món ăn đã tồn tại");
			}
		} catch (Exception e) {
		}
	}

	// UPLOAD IMAGE

	public void upload(FileUploadEvent event) {
		try {
			InputStream in = event.getFile().getInputstream();
			// Ghi inputStream vao 1 OutputStream
			// Tao outputstream -> new doi tuong xong truyen duong dan +
			// fileName
			OutputStream out = new FileOutputStream(new File(destination + "/" + event.getFile().getFileName()));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();
			System.err.println("New file created!");

			// Company temp =
			// companyService.findById(customer.getCompany().getId());
			// temp.setLogo(event.getFile().getFileName());
			// companyService.update(temp);
			PrimeFaces.current().executeScript("PF('dialogCreateSuccessLogo').show()");

		} catch (IOException e) {
			System.out.println(e.getMessage());
			PrimeFaces.current().executeScript("PF('dialogCreateErrorLogo').show()");
		}

	}
	// END UPLOAD IMAGE

	// NEW add new category food
	public void addNewItem() {
		if (courseTypeNew.getName() != null && !courseTypeNew.getName().isEmpty()) {
			courseTypeNew.setCreatedDate(new Date());
			courseTypeNew.setCreatedUser(member.getCode());
			// check co trung ten hay khong
			List<CourseType> checkExistName = COURSE_TYPE_SERVICE.findByName(courseTypeNew.getName());
			if (checkExistName.isEmpty()) {
				CourseType cfNew = COURSE_TYPE_SERVICE.create(courseTypeNew);
				if (cfNew != null) {
					// Notification.NOTI_SUCCESS("Thành công");
					MessageView.INFO("Thành công");
					allCourseType = COURSE_TYPE_SERVICE.findAllNew();
					courseTypeNew = new CourseType();
					PrimeFaces current = PrimeFaces.current();
					current.executeScript("PF('widgetThemMonAn').hide();");
				} else {
					// Notification.NOTI_ERROR("Không thành công");
					MessageView.ERROR("Không thành công");
				}
			} else {
				// Notification.NOTI_ERROR("Tên món ăn đã tồn tại");
				MessageView.ERROR("Tên món ăn đã tồn tại");
			}
		} else {
			// Notification.NOTI_WARN("Vui lòng điền đầy đủ thông tin");
			MessageView.ERROR("Vui lòng nhập tên món ăn");
		}
	}

	@Override
	protected Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<CourseType> getAllCourseType() {
		return allCourseType;
	}

	public void setAllCourseType(List<CourseType> allCourseType) {
		this.allCourseType = allCourseType;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public List<CourseType> getCourseTypesFilter() {
		return courseTypesFilter;
	}

	public void setCourseTypesFilter(List<CourseType> courseTypesFilter) {
		this.courseTypesFilter = courseTypesFilter;
	}

	public CourseType getCourseTypeNew() {
		return courseTypeNew;
	}

	public void setCourseTypeNew(CourseType courseTypeNew) {
		this.courseTypeNew = courseTypeNew;
	}

	public CourseType getCourseTypeUpdate() {
		return courseTypeUpdate;
	}

	public void setCourseTypeUpdate(CourseType courseTypeUpdate) {
		this.courseTypeUpdate = courseTypeUpdate;
	}
}
