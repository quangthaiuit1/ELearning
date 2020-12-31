package trong.lixco.com.bean.elearning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.bean.entities.QuestionAndAnswer;
import trong.lixco.com.bean.staticentity.MessageView;
import trong.lixco.com.ejb.service.elearning.AnswerService;
import trong.lixco.com.ejb.service.elearning.PlanDetailSkillService;
import trong.lixco.com.ejb.service.elearning.QuestionService;
import trong.lixco.com.ejb.service.elearning.TestResultService;
import trong.lixco.com.jpa.entities.Answer;
import trong.lixco.com.jpa.entities.PlanDetailSkill;
import trong.lixco.com.jpa.entities.Question;
import trong.lixco.com.jpa.entities.TestResult;

@Named
@ViewScoped
public class TestBean extends AbstractBean {

	private static final long serialVersionUID = 1L;
	private List<QuestionAndAnswer> qAndAnswers;
	private long pdsId = 0; // plan detail skill
	private Member member;

	// private List<Question> questionsBySkill;
	private Answer[] answersResp; // dap an tra loi

	@Inject
	private QuestionService QUESTION_SERVICE;
	@Inject
	private AnswerService ANSWER_SERVICE;
	@Inject
	private PlanDetailSkillService PLAN_DETAIL_SKILL_SERVICE;
	@Inject
	private TestResultService TEST_RESULT_SERVICE;

	@Override
	protected void initItem() {
		long skillId = getParamSkillId();
		member = getAccount().getMember();
		pdsId = getParamPDSId();
		// cau hoi theo ki nang
		List<Question> questionsBySkill = QUESTION_SERVICE.findBySkill(skillId);
		qAndAnswers = new ArrayList<>();
		for (int i = 0; i < questionsBySkill.size(); i++) {
			List<Answer> answers = ANSWER_SERVICE.findByQuestion(questionsBySkill.get(i).getId());
			if (!answers.isEmpty() && answers != null) {
				QuestionAndAnswer qa = new QuestionAndAnswer(questionsBySkill.get(i), answers);
				qAndAnswers.add(qa);
				answersResp = new Answer[qAndAnswers.size() + 1];
			}
		}
	}

	public long getParamSkillId() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String setofIdTemp = request.getParameter("skillid");
		if (setofIdTemp.equals("null")) {
			return 0;
		}
		return Long.parseLong(setofIdTemp);
	}

	public void redirectTo() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			FacesContext fContext = FacesContext.getCurrentInstance();
			ExternalContext extContext = fContext.getExternalContext();
			extContext.redirect("http://192.168.0.132:8880/elearning/pages/nhanvien/dskhoahoc.htm");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public long getParamPDSId() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String setofIdTemp = request.getParameter("pdsid");
		if (setofIdTemp.equals("null")) {
			return 0;
		}
		return Long.parseLong(setofIdTemp);
	}

	public void submitTest() {
		try {

			for (int i = 1; i < answersResp.length; i++) {
				if (answersResp[i] == null) {
					MessageView.ERROR("Vui lòng hoàn thành toàn bộ bài thi");
					return;
				}
			}
			PlanDetailSkill p = PLAN_DETAIL_SKILL_SERVICE.findById(pdsId);
			for (int i = 1; i < answersResp.length; i++) {
				try {
					TestResult t = new TestResult(member.getCode(), answersResp[i], p);
					TEST_RESULT_SERVICE.create(t);
				} catch (Exception e) {
					e.printStackTrace();
					MessageView.ERROR("Lỗi");
					return;
				}
			}
			int socaudung = 0;
			for (int i = 1; i < answersResp.length; i++) {
				if (answersResp[i].isIs_true()) {
					socaudung = socaudung + 1;
				}
			}

			double sodiem = ((double) 10 / (double) qAndAnswers.size()) * (double) socaudung;
			p.setScore(sodiem);
			PlanDetailSkill pd = PLAN_DETAIL_SKILL_SERVICE.update(p);
			if (pd != null && pd.getId() != null) {
				MessageView.INFO("Thành công");
				// redirectTo();
				return;
			} else {
				MessageView.ERROR("Lỗi");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

	public List<QuestionAndAnswer> getqAndAnswers() {
		return qAndAnswers;
	}

	public void setqAndAnswers(List<QuestionAndAnswer> qAndAnswers) {
		this.qAndAnswers = qAndAnswers;
	}

	public Answer[] getAnswersResp() {
		return answersResp;
	}

	public void setAnswersResp(Answer[] answersResp) {
		this.answersResp = answersResp;
	}

}
