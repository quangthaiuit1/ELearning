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
import trong.lixco.com.jpa.entities.PlanDetailSkill;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PlanDetailSkillService extends AbstractService<PlanDetailSkill> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<PlanDetailSkill> getEntityClass() {
		return PlanDetailSkill.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	public PlanDetailSkill findByCourseAndPlan(long courseId, long planId) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PlanDetailSkill> cq = cb.createQuery(PlanDetailSkill.class);
		Root<PlanDetailSkill> root = cq.from(PlanDetailSkill.class);
		List<Predicate> queries = new ArrayList<>();
		if (courseId != 0) {
			Predicate query = cb.equal(root.get("course").get("id"), courseId);
			queries.add(query);
		}
		if (planId != 0) {
			Predicate query = cb.equal(root.get("plan").get("id"), planId);
			queries.add(query);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<PlanDetailSkill> query = em.createQuery(cq);
		List<PlanDetailSkill> results = query.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return new PlanDetailSkill();
		}
	}

	public List<PlanDetailSkill> findBySkillAndPlanDetail(long skillId, long plandetailId) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PlanDetailSkill> cq = cb.createQuery(PlanDetailSkill.class);
		Root<PlanDetailSkill> root = cq.from(PlanDetailSkill.class);
		List<Predicate> queries = new ArrayList<>();
		if (skillId != 0) {
			Predicate foodNameQuery = cb.equal(root.get("skill").get("id"), skillId);
			queries.add(foodNameQuery);
		}
		if (plandetailId != 0) {
			Predicate foodNameQuery = cb.equal(root.get("plan_detail").get("id"), plandetailId);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<PlanDetailSkill> query = em.createQuery(cq);
		List<PlanDetailSkill> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<>();
		}
	}
}
