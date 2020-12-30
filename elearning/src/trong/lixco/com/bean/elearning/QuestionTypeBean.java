package trong.lixco.com.bean.elearning;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;

import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.bean.staticentity.MessageView;
import trong.lixco.com.bean.staticentity.Notification;
import trong.lixco.com.ejb.service.elearning.QuestionTypeService;
import trong.lixco.com.jpa.entities.QuestionType;
import trong.lixco.com.util.Notify;

@Named
@ViewScoped
public class QuestionTypeBean extends AbstractBean<QuestionType> {
	private static final long serialVersionUID = 1L;
	private List<QuestionType> allQuestionType;
	private List<QuestionType> questionTypesFilter;
	private QuestionType questionTypeNew;
	private QuestionType questionTypeUpdate;

	private Notify notify;
	private Member member;
	// Thu muc chua image

	@Inject
	private QuestionTypeService QUESTION_TYPE_SERVICE;

	@Override
	protected void initItem() {
		questionTypeNew = new QuestionType();
		questionTypeUpdate = new QuestionType();
		notify = new Notify(FacesContext.getCurrentInstance());
		allQuestionType = new ArrayList<QuestionType>();
		// categoryFoodsRemove = new ArrayList<>();
		try {
			allQuestionType = QUESTION_TYPE_SERVICE.findAllNew();
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

	public void deleteRow(QuestionType item) {
		for (QuestionType question : allQuestionType) {
			if (question.getId() == item.getId()) {
				try {
					boolean delete = QUESTION_TYPE_SERVICE.delete(item);
					if (delete) {
						allQuestionType = QUESTION_TYPE_SERVICE.findAllNew();
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
			List<QuestionType> checkExistName = QUESTION_TYPE_SERVICE.findByName(questionTypeUpdate.getName());
			boolean isExist = false;
			if (checkExistName.size() < 2) {
				if (checkExistName.size() == 1) {
					if (checkExistName.get(0).getId() != questionTypeUpdate.getId()) {
						isExist = true;
					}
				}
				// ten chua co
				if (!isExist) {
					questionTypeUpdate.setModifiedDate(new Date());
					questionTypeUpdate.setModifiedUser(member.getCode());
					// truong hop khong co phan tu nao trung -> cap nhat ten
					QuestionType cfUpdate = QUESTION_TYPE_SERVICE.update(questionTypeUpdate);
					if (cfUpdate != null) {
						// Notification.NOTI_SUCCESS("Thành công");
						MessageView.INFO("Thành công");
						allQuestionType = QUESTION_TYPE_SERVICE.findAllNew();
						questionTypeUpdate = new QuestionType();
						PrimeFaces current = PrimeFaces.current();
						current.executeScript("PF('widgetCapNhatMonAn').hide();");
					} else {
						Notification.NOTI_ERROR("Thất bại");
					}
				} else {
					allQuestionType = QUESTION_TYPE_SERVICE.findAllNew();
					Notification.NOTI_ERROR("Tên món ăn đã tồn tại");
				}
			} else {
				allQuestionType = QUESTION_TYPE_SERVICE.findAllNew();
				Notification.NOTI_ERROR("Tên món ăn đã tồn tại");
			}
		} catch (Exception e) {
		}
	}

	// UPLOAD IMAGE


	// NEW add new category food
	public void addNewItem() {
		if (questionTypeNew.getName() != null && !questionTypeNew.getName().isEmpty()) {
			questionTypeNew.setCreatedDate(new Date());
			questionTypeNew.setCreatedUser(member.getCode());
			// check co trung ten hay khong
			List<QuestionType> checkExistName = QUESTION_TYPE_SERVICE.findByName(questionTypeNew.getName());
			if (checkExistName.isEmpty()) {
				QuestionType cfNew = QUESTION_TYPE_SERVICE.create(questionTypeNew);
				if (cfNew != null) {
					// Notification.NOTI_SUCCESS("Thành công");
					MessageView.INFO("Thành công");
					allQuestionType = QUESTION_TYPE_SERVICE.findAllNew();
					questionTypeNew = new QuestionType();
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

	public List<QuestionType> getAllQuestionType() {
		return allQuestionType;
	}

	public void setAllQuestionType(List<QuestionType> allQuestionType) {
		this.allQuestionType = allQuestionType;
	}

	public List<QuestionType> getQuestionTypesFilter() {
		return questionTypesFilter;
	}

	public void setQuestionTypesFilter(List<QuestionType> questionTypesFilter) {
		this.questionTypesFilter = questionTypesFilter;
	}

	public QuestionType getQuestionTypeNew() {
		return questionTypeNew;
	}

	public void setQuestionTypeNew(QuestionType questionTypeNew) {
		this.questionTypeNew = questionTypeNew;
	}

	public QuestionType getQuestionTypeUpdate() {
		return questionTypeUpdate;
	}

	public void setQuestionTypeUpdate(QuestionType questionTypeUpdate) {
		this.questionTypeUpdate = questionTypeUpdate;
	}
}
