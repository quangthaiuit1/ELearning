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
import trong.lixco.com.jpa.entities.CoursePositionJob;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CoursePositionJobService extends AbstractService<CoursePositionJob> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<CoursePositionJob> getEntityClass() {
		return CoursePositionJob.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	public List<CoursePositionJob> findByPosition(String positionCode) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CoursePositionJob> cq = cb.createQuery(CoursePositionJob.class);
		Root<CoursePositionJob> root = cq.from(CoursePositionJob.class);
		List<Predicate> queries = new ArrayList<>();
		if (positionCode != null) {
			Predicate foodNameQuery = cb.equal(root.get("position_job_code"), positionCode);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<CoursePositionJob> query = em.createQuery(cq);
		List<CoursePositionJob> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<CoursePositionJob>();
		}
	}

	public CoursePositionJob findByName(String name) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CoursePositionJob> cq = cb.createQuery(CoursePositionJob.class);
		Root<CoursePositionJob> root = cq.from(CoursePositionJob.class);
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
		TypedQuery<CoursePositionJob> query = em.createQuery(cq);
		List<CoursePositionJob> results = query.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return new CoursePositionJob();
		}
	}
}
