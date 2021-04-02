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
import trong.lixco.com.jpa.entities.Plan;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PlanService extends AbstractService<Plan> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<Plan> getEntityClass() {
		return Plan.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	public List<Plan> findByDepartAndYear(String departCode, int year) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
		Root<Plan> root = cq.from(Plan.class);
		List<Predicate> queries = new ArrayList<>();
		if (departCode != null) {
			Predicate foodNameQuery = cb.equal(root.get("department_code"), departCode);
			queries.add(foodNameQuery);
		}
		if (year != 0) {
			Predicate foodNameQuery = cb.equal(root.get("year"), year);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<Plan> query = em.createQuery(cq);
		List<Plan> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<Plan>();
		}
	}

	public Plan findByEmployeeCode(String employeeCode) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
		Root<Plan> root = cq.from(Plan.class);
		List<Predicate> queries = new ArrayList<>();
		if (employeeCode != null) {
			Predicate foodNameQuery = cb.equal(root.get("employee_code"), employeeCode);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<Plan> query = em.createQuery(cq);
		List<Plan> results = query.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return new Plan();
		}
	}

	public List<Plan> findByEmplCodeAndYear(String employeeCode, int year) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Plan> cq = cb.createQuery(Plan.class);
		Root<Plan> root = cq.from(Plan.class);
		List<Predicate> queries = new ArrayList<>();
		if (employeeCode != null) {
			Predicate foodNameQuery = cb.equal(root.get("employee_code"), employeeCode);
			queries.add(foodNameQuery);
		}
		if (year != 0) {
			Predicate foodNameQuery = cb.equal(root.get("year"), year);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<Plan> query = em.createQuery(cq);
		List<Plan> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<>();
		}
	}

}
