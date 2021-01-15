package trong.lixco.com.bean.elearning;

import javax.inject.Named;

import org.jboss.logging.Logger;
import org.omnifaces.cdi.ViewScoped;

import trong.lixco.com.account.servicepublics.Member;
import trong.lixco.com.bean.AbstractBean;
import trong.lixco.com.jpa.entities.Course;

@Named
@ViewScoped
public class ChamDiemBean extends AbstractBean {

	private static final long serialVersionUID = 1L;
	private Member member;

	@Override
	protected void initItem() {

	}

	@Override
	protected Logger getLogger() {
		return null;
	}

}
