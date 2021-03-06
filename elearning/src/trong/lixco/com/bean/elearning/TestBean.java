package trong.lixco.com.bean.elearning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import trong.lixco.com.bean.entities.QuestionAndAnswerTuLuan;
import trong.lixco.com.bean.staticentity.MessageView;
import trong.lixco.com.bean.staticentity.QuestionTypeUtil;
import trong.lixco.com.ejb.service.elearning.AnswerService;
import trong.lixco.com.ejb.service.elearning.PlanDetailService;
import trong.lixco.com.ejb.service.elearning.PlanDetailSkillService;
import trong.lixco.com.ejb.service.elearning.QuestionService;
import trong.lixco.com.ejb.service.elearning.TestResultService;
import trong.lixco.com.jpa.entities.Answer;
import trong.lixco.com.jpa.entities.PlanDetail;
import trong.lixco.com.jpa.entities.PlanDetailSkill;
import trong.lixco.com.jpa.entities.Question;
import trong.lixco.com.jpa.entities.TestResult;

@Named
@ViewScoped
public class TestBean extends AbstractBean {

	private static final long serialVersionUID = 1L;
	private List<QuestionAndAnswer> qAndAnswers;
	private List<QuestionAndAnswerTuLuan> qAndAnswerTuLuans;
	private long pdsId = 0; // plan detail skill
	private long skillid = 0;
	private Member member;
	private boolean isHaveTracNghiem = false;
	private boolean isHaveTuLuan = false;

	// private List<Question> questionsBySkill;
	private Answer[] answersResp; // dap an tra loi

	@Inject
	private QuestionService QUESTION_SERVICE;
	@Inject
	private AnswerService ANSWER_SERVICE;
	@Inject
	private PlanDetailSkillService PLAN_DETAIL_SKILL_SERVICE;
	@Inject
	private PlanDetailService PLAN_DETAIL_SERVICE;
	@Inject
	private TestResultService TEST_RESULT_SERVICE;

	@Override
	protected void initItem() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		String param1 = params.get("pdsid");
		String param2 = params.get("skillid");
		if (param1 != null && param2 != null) {
			pdsId = Long.parseLong(param1);
			skillid = Long.parseLong(param2);
		}

		member = getAccount().getMember();
		// cau hoi theo ki nang
		List<Question> questionsBySkill = QUESTION_SERVICE.findBySkill(skillid);
		qAndAnswers = new ArrayList<>();
		qAndAnswerTuLuans = new ArrayList<>();
		for (int i = 0; i < questionsBySkill.size(); i++) {
			// check co tu luan hoac trac nghiem khong
			// trac nghiem
			if (questionsBySkill.get(i).getQuestion_type().getId() == QuestionTypeUtil.TRAC_NGHIEM_ID) {
				isHaveTracNghiem = true;
				List<Answer> answers = ANSWER_SERVICE.findByQuestion(questionsBySkill.get(i).getId());
				if (!answers.isEmpty() && answers != null) {
					QuestionAndAnswer qa = new QuestionAndAnswer(questionsBySkill.get(i), answers);
					qAndAnswers.add(qa);
					answersResp = new Answer[qAndAnswers.size() + 1];
				}
			}
			// tu luan
			if (questionsBySkill.get(i).getQuestion_type().getId() == QuestionTypeUtil.TU_LUAN_ID) {
				isHaveTuLuan = true;
				QuestionAndAnswerTuLuan qTL = new QuestionAndAnswerTuLuan(questionsBySkill.get(i));
				qAndAnswerTuLuans.add(qTL);
			}

		}
	}

	// public long getParamSkillId() {
	// HttpServletRequest request = (HttpServletRequest)
	// FacesContext.getCurrentInstance().getExternalContext()
	// .getRequest();
	// String setofIdTemp = request.getParameter("skillid");
	// if (setofIdTemp == null || setofIdTemp.equals("null")) {
	// return 0;
	// }
	// return Long.parseLong(setofIdTemp);
	// }

	public void redirectTo(long pdid) {
		try {
			FacesContext fContext = FacesContext.getCurrentInstance();
			ExternalContext extContext = fContext.getExternalContext();
			String path = "http://192.168.0.132:8880/elearning/pages/nhanvien/chitietkhoahoc.htm?pdid=" + pdid;
			extContext.redirect(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public long getParamPDSId() {
	// HttpServletRequest request = (HttpServletRequest)
	// FacesContext.getCurrentInstance().getExternalContext()
	// .getRequest();
	// // FacesContext fContext = FacesContext.getCurrentInstance();
	// // ExternalContext extContext = fContext.getExternalContext();
	// // Map<String, String> parameterMap = (Map<String, String>)
	// // extContext.getRequestParameterMap();
	// // String setofIdTemp = parameterMap.get("pdsid");
	// // if (setofIdTemp == null || setofIdTemp.equals("null")) {
	// // return 0;
	// // }
	//
	// FacesContext fc = FacesContext.getCurrentInstance();
	// Map<String, String> params =
	// fc.getExternalContext().getRequestParameterMap();
	// String param1 = params.get("pdsid");
	// String param2 = params.get("skillid");
	//
	// return Long.parseLong(param1);
	// }

	public void submitTest() {
		try {
			if (answersResp == null) {
				return;
			}
			for (int i = 1; i < answersResp.length; i++) {
				if (answersResp[i] == null) {
					MessageView.ERROR("Vui lòng hoàn thành toàn bộ bài thi");
					return;
				}
			}
			// kiem tra bai kiem tra da duoc thuc hien hay chua
			List<TestResult> trs = TEST_RESULT_SERVICE.findByPDSId(pdsId);
			if (!trs.isEmpty()) {
				MessageView.ERROR("Không thể thực hiện lại bài thi");
				return;
			}
			PlanDetailSkill p = PLAN_DETAIL_SKILL_SERVICE.findById(pdsId);
			for (int i = 1; i < answersResp.length; i++) {
				try {
					TestResult t = new TestResult(member.getCode(), answersResp[i].getName(),
							answersResp[i].getQuestion(), p);
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
			p.setScore_tracnghiem(sodiem);
			// p.setScore_total(sodiem);
			PlanDetailSkill pds = PLAN_DETAIL_SKILL_SERVICE.update(p);
			if (pds != null && pds.getId() != null) {
				redirectTo(pds.getPlan_detail().getId());
				// handle tinh diem trung binh khoa hoc
				double totalScore = 0;
				List<PlanDetailSkill> pdss = PLAN_DETAIL_SKILL_SERVICE.findBySkillAndPlanDetail(0,
						pds.getPlan_detail().getId());
				for (PlanDetailSkill s : pdss) {
					totalScore = totalScore + s.getScore_tracnghiem();
				}
				double avg = totalScore / (double) pdss.size();
				PlanDetail pd = pds.getPlan_detail();
				pd.setAvg_score(avg);
				PLAN_DETAIL_SERVICE.update(pd);
			} else {
				MessageView.ERROR("Lỗi");
				return;
			}
			// luu dap an tu luan
			for (QuestionAndAnswerTuLuan q : qAndAnswerTuLuans) {
				TestResult t = new TestResult(member.getCode(), q.getAnswer(), q.getQuestion(), p);
				TEST_RESULT_SERVICE.create(t);
			}
			MessageView.INFO("Thành công");

		} catch (Exception e) {
			e.printStackTrace();
			MessageView.ERROR("Lỗi");
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

	public boolean isHaveTracNghiem() {
		return isHaveTracNghiem;
	}

	public void setHaveTracNghiem(boolean isHaveTracNghiem) {
		this.isHaveTracNghiem = isHaveTracNghiem;
	}

	public boolean isHaveTuLuan() {
		return isHaveTuLuan;
	}

	public void setHaveTuLuan(boolean isHaveTuLuan) {
		this.isHaveTuLuan = isHaveTuLuan;
	}

	public List<QuestionAndAnswerTuLuan> getqAndAnswerTuLuans() {
		return qAndAnswerTuLuans;
	}

	public void setqAndAnswerTuLuans(List<QuestionAndAnswerTuLuan> qAndAnswerTuLuans) {
		this.qAndAnswerTuLuans = qAndAnswerTuLuans;
	}

}
