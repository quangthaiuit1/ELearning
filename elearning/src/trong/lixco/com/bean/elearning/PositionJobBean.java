package trong.lixco.com.bean.elearning;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.bean.entities.PositionJobData;
import trong.lixco.com.bean.entities.PositionJobDataService;
import trong.lixco.com.bean.staticentity.MessageView;
import trong.lixco.com.ejb.service.elearning.PositionJobService;
import trong.lixco.com.jpa.entities.PositionJob;

@Named
@ViewScoped
public class PositionJobBean extends AbstractBean<PositionJob> {
	private static final long serialVersionUID = 1L;
	private PositionJobData[] positionsCenterArr;
	// private List<PositionJobData> positionsCenterList;
	// private List<PositionJobData> positionsCenterFilter;

	private List<PositionJob> positions;
	private List<PositionJob> positionsFilter;
	private Member member;

	@Inject
	private PositionJobService POSITION_JOB_SERVICE;

	@Override
	protected void initItem() {
		positions = POSITION_JOB_SERVICE.findAll();
		member = getAccount().getMember();
	}

	public void syncFromCenter() {
		positionsCenterArr = PositionJobDataService.tatcaphongban();
		// xoa toan bo o database
		for (PositionJob p : positions) {
			boolean deleteSuccess = POSITION_JOB_SERVICE.delete(p);
			if (!deleteSuccess) {
				MessageView.ERROR("Lỗi");
				return;
			}
		}
		// kiem tra luu thanh cong hay khong
		boolean isSuccess = true;
		for (int i = 0; i < positionsCenterArr.length; i++) {
			// date, create user, code, name, code depart
			PositionJob pNew = new PositionJob(new Date(), member.getCode(), positionsCenterArr[i].getCode(),
					positionsCenterArr[i].getName(), positionsCenterArr[i].getCodeDepart());
			try {
				POSITION_JOB_SERVICE.create(pNew);
			} catch (Exception e) {
				isSuccess = false;
				e.printStackTrace();
			}
		}
		if (isSuccess) {
			MessageView.INFO("Thành công");
		} else {
			MessageView.ERROR("Lỗi");
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

}
