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
import trong.lixco.com.jpa.entities.CourseRating;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CourseRatingService extends AbstractService<CourseRating> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<CourseRating> getEntityClass() {
		return CourseRating.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	public Course createNew(Course entity) {
		em.persist(entity);
		return em.merge(entity);
	}

	public List<CourseRating> findByCourse(long courseId) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CourseRating> cq = cb.createQuery(CourseRating.class);
		Root<CourseRating> root = cq.from(CourseRating.class);
		List<Predicate> queries = new ArrayList<>();
		if (courseId != 0) {
			Predicate foodNameQuery = cb.equal(root.get("course").get("id"), courseId);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<CourseRating> query = em.createQuery(cq);
		List<CourseRating> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<CourseRating>();
		}
	}

	public List<CourseRating> findByCourseAndEmpl(long courseId, String emplCode) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CourseRating> cq = cb.createQuery(CourseRating.class);
		Root<CourseRating> root = cq.from(CourseRating.class);
		List<Predicate> queries = new ArrayList<>();
		if (courseId != 0) {
			Predicate foodNameQuery = cb.equal(root.get("course").get("id"), courseId);
			queries.add(foodNameQuery);
		}
		if (emplCode != null) {
			Predicate foodNameQuery = cb.equal(root.get("employee_code"), emplCode);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<CourseRating> query = em.createQuery(cq);
		List<CourseRating> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<CourseRating>();
		}
	}
}
