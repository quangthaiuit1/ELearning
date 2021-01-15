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
import trong.lixco.com.jpa.entities.Course;
import trong.lixco.com.jpa.entities.PlanDetail;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PlanDetailService extends AbstractService<PlanDetail> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<PlanDetail> getEntityClass() {
		return PlanDetail.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	public PlanDetail findByCourseAndPlan(long courseId, long planId) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PlanDetail> cq = cb.createQuery(PlanDetail.class);
		Root<PlanDetail> root = cq.from(PlanDetail.class);
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
		TypedQuery<PlanDetail> query = em.createQuery(cq);
		List<PlanDetail> results = query.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return new PlanDetail();
		}
	}

	public List<PlanDetail> findByPlan(long planId) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PlanDetail> cq = cb.createQuery(PlanDetail.class);
		Root<PlanDetail> root = cq.from(PlanDetail.class);
		List<Predicate> queries = new ArrayList<>();
		if (planId != 0) {
			Predicate foodNameQuery = cb.equal(root.get("plan").get("id"), planId);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<PlanDetail> query = em.createQuery(cq);
		List<PlanDetail> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<>();
		}
	}

	public PlanDetail findByCourseAndEmployeeCode(long courseId, String employeeCode) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PlanDetail> cq = cb.createQuery(PlanDetail.class);
		Root<PlanDetail> root = cq.from(PlanDetail.class);
		List<Predicate> queries = new ArrayList<>();
		if (courseId != 0) {
			Predicate query = cb.equal(root.get("course").get("id"), courseId);
			queries.add(query);
		}
		if (employeeCode != null) {
			Predicate query = cb.equal(root.get("plan").get("employee_code"), employeeCode);
			queries.add(query);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<PlanDetail> query = em.createQuery(cq);
		List<PlanDetail> results = query.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return new PlanDetail();
		}
	}
}
