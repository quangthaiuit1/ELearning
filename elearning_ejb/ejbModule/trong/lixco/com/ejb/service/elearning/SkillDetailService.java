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
import trong.lixco.com.jpa.entities.SkillDetail;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SkillDetailService extends AbstractService<SkillDetail> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<SkillDetail> getEntityClass() {
		return SkillDetail.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	public List<SkillDetail> findBySkill(long skillId) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SkillDetail> cq = cb.createQuery(SkillDetail.class);
		Root<SkillDetail> root = cq.from(SkillDetail.class);
		List<Predicate> queries = new ArrayList<>();
		if (skillId != 0) {
			Predicate foodNameQuery = cb.equal(root.get("skill").get("id"), skillId);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<SkillDetail> query = em.createQuery(cq);
		List<SkillDetail> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<SkillDetail>();
		}
	}

	public SkillDetail findByName(String name) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SkillDetail> cq = cb.createQuery(SkillDetail.class);
		Root<SkillDetail> root = cq.from(SkillDetail.class);
		List<Predicate> queries = new ArrayList<>();
		if (name != null) {
			Predicate foodNameQuery = cb.equal(root.get("name"), name);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<SkillDetail> query = em.createQuery(cq);
		List<SkillDetail> results = query.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return new SkillDetail();
		}
	}
}
