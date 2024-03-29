package trong.lixco.com.bean.elearning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.bean.staticentity.MessageView;
import trong.lixco.com.ejb.service.elearning.AnswerService;
import trong.lixco.com.ejb.service.elearning.QuestionService;
import trong.lixco.com.ejb.service.elearning.QuestionTypeService;
import trong.lixco.com.ejb.service.elearning.SkillService;
import trong.lixco.com.jpa.entities.Answer;
import trong.lixco.com.jpa.entities.Question;
import trong.lixco.com.jpa.entities.QuestionType;

@Named
@ViewScoped
public class CreateQuestionBean extends AbstractBean<Question> {
	private static final long serialVersionUID = 1L;
	private Long skillId;
	private Member member;
	private QuestionType questionTypeSelected;
	private long questionTypeCurrent;
	private List<Answer> answersNew;
	private List<QuestionType> allQType;
	private String questionNameNew;
	private Question questionNew;
	private String fileNameImageCurrent = "noimage.png";

	// Cac loai cau hoi
	private long TRAC_NGHIEM;
	private long TU_LUAN;

	@Inject
	private QuestionTypeService QUESTION_TYPE_SERVICE;
	@Inject
	private SkillService SKILL_SERVICE;
	@Inject
	private QuestionService QUESTION_SERVICE;
	@Inject
	private AnswerService ANSWER_SERVICE;

	@Override
	protected void initItem() {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			String skillIdTemp = request.getParameter("sid");
			if (!skillIdTemp.equals("null")) {
				skillId = Long.parseLong(skillIdTemp);
			}
			member = getAccount().getMember();
			TRAC_NGHIEM = 1;
			TU_LUAN = 2;
			allQType = QUESTION_TYPE_SERVICE.findAll();
			if (allQType.get(0).getId() == TRAC_NGHIEM) {
				questionTypeCurrent = TRAC_NGHIEM;
			}

			if (questionTypeCurrent == TRAC_NGHIEM) {
				answersNew = new ArrayList<>();
				questionNew = new Question();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ajaxHandleChooseQType() {
		try {
			if (questionTypeSelected.getId() != null) {
				questionTypeCurrent = questionTypeSelected.getId();
				if (questionTypeCurrent == TRAC_NGHIEM) {
					Answer temp = new Answer();
					answersNew.add(temp);
				}
				questionNew = new Question();
			}
		} catch (Exception e) {
		}
	}

	public void addRowNew() {
		Answer a = new Answer();
		answersNew.add(a);
	}

	public void createQuestion() {
		try {
			if (skillId == null || skillId == 0) {
				MessageView.ERROR("Không thể tạo mới");
				return;
			}
			questionNew.setCreatedDate(new Date());
			questionNew.setCreatedUser(member.getCode());
			questionNew.setSkill(SKILL_SERVICE.findById(skillId));
			questionNew.setQuestion_type(QUESTION_TYPE_SERVICE.findById(questionTypeCurrent));
			if (StringUtils.isNotEmpty(fileNameImageCurrent)) {
				questionNew.setImage_name(fileNameImageCurrent);
			}
			questionNew = QUESTION_SERVICE.create(questionNew);
			// tao dap an
			if (questionNew.getId() != null && questionNew.getId() != 0) {
				for (int i = 0; i < answersNew.size(); i++) {
					answersNew.get(i).setCreatedDate(new Date());
					answersNew.get(i).setCreatedUser(member.getCode());
					answersNew.get(i).setQuestion(questionNew);
					try {
						ANSWER_SERVICE.create(answersNew.get(i));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				fileNameImageCurrent = "noimage.png";
				MessageView.INFO("Thành công");
				questionNew = new Question();
				answersNew = new ArrayList<>();
			} else {
				MessageView.ERROR("Lỗi");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteRow(Answer item) {
		try {
			if (StringUtils.isNotEmpty(item.getName())) {
				List<Answer> temp = new ArrayList<>();
				for (Answer r : answersNew) {
					if (!r.getName().equals(item.getName())) {
						temp.add(r);
					}
				}
				answersNew = new ArrayList<>();
				answersNew.addAll(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Logger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}

	public QuestionType getQuestionTypeSelected() {
		return questionTypeSelected;
	}

	public void setQuestionTypeSelected(QuestionType questionTypeSelected) {
		this.questionTypeSelected = questionTypeSelected;
	}

	public long getTRAC_NGHIEM() {
		return TRAC_NGHIEM;
	}

	public void setTRAC_NGHIEM(long tRAC_NGHIEM) {
		TRAC_NGHIEM = tRAC_NGHIEM;
	}

	public long getTU_LUAN() {
		return TU_LUAN;
	}

	public void setTU_LUAN(long tU_LUAN) {
		TU_LUAN = tU_LUAN;
	}

	public long getQuestionTypeCurrent() {
		return questionTypeCurrent;
	}

	public void setQuestionTypeCurrent(long questionTypeCurrent) {
		this.questionTypeCurrent = questionTypeCurrent;
	}

	public List<Answer> getAnswersNew() {
		return answersNew;
	}

	public void setAnswersNew(List<Answer> answersNew) {
		this.answersNew = answersNew;
	}

	public List<QuestionType> getAllQType() {
		return allQType;
	}

	public void setAllQType(List<QuestionType> allQType) {
		this.allQType = allQType;
	}

	public String getQuestionNameNew() {
		return questionNameNew;
	}

	public void setQuestionNameNew(String questionNameNew) {
		this.questionNameNew = questionNameNew;
	}

	public Question getQuestionNew() {
		return questionNew;
	}

	public void setQuestionNew(Question questionNew) {
		this.questionNew = questionNew;
	}

	public String getFileNameImageCurrent() {
		return fileNameImageCurrent;
	}

	public void setFileNameImageCurrent(String fileNameImageCurrent) {
		this.fileNameImageCurrent = fileNameImageCurrent;
	}

	// private String destination = "C:\\tmp\\";
	private String destination = "D:\\STORAGE-ELEARNING\\IMAGES\\";

	// private String destination;

	public void upload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		// Do what you want with the file
		try {
			copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
			// JSFUtil.adicionarMensajeSuceso("Imagen subida correctamente");
			PrimeFaces.current().ajax().update("formCreateQuestion:graphicImageQuestion");
		} catch (IOException e) {
			e.printStackTrace();
			// JSFUtil.adicionarMensajeError(e.getMessage());
		}

	}

	public void copyFile(String fileName, InputStream in) {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		// String filePath =
		// ec.getRealPath(String.format("/resources/images/%s","/"));
		// System.out.println(filePath);
		try {

			// write the inputStream to a FileOutputStream
			fileNameImageCurrent = System.currentTimeMillis() + "-" + fileName;
			String fileNameFull = destination + fileNameImageCurrent;
			OutputStream out = new FileOutputStream(new File(fileNameFull));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

			System.out.println("New file created!");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
