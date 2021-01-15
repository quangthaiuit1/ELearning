package trong.lixco.com.ejb.service.elearning;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import trong.lixco.com.ejb.service.AbstractService;
import trong.lixco.com.jpa.entities.LogTimeSeenVideo;
import trong.lixco.com.jpa.entities.PlanDetail;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LogTimeSeenVideoService extends AbstractService<LogTimeSeenVideo> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<LogTimeSeenVideo> getEntityClass() {
		return LogTimeSeenVideo.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	public LogTimeSeenVideo findByPDSkillAndSDetail(long pdsId, long sdId) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<LogTimeSeenVideo> cq = cb.createQuery(LogTimeSeenVideo.class);
		Root<LogTimeSeenVideo> root = cq.from(LogTimeSeenVideo.class);
		List<Predicate> queries = new ArrayList<>();
		if (pdsId != 0) {
			Predicate query = cb.equal(root.get("plan_detail_skill").get("id"), pdsId);
			queries.add(query);
		}
		if (sdId != 0) {
			Predicate query = cb.equal(root.get("skill_detail").get("id"), sdId);
			queries.add(query);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<LogTimeSeenVideo> query = em.createQuery(cq);
		List<LogTimeSeenVideo> results = query.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return new LogTimeSeenVideo();
		}
	}
}
