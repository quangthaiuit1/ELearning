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
import trong.lixco.com.jpa.entities.CourseType;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CourseService extends AbstractService<Course> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<Course> getEntityClass() {
		return Course.class;
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

	public List<Course> findByCourseType(long courseTypeId) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Course> cq = cb.createQuery(Course.class);
		Root<Course> root = cq.from(Course.class);
		List<Predicate> queries = new ArrayList<>();
		if (courseTypeId != 0) {
			Predicate foodNameQuery = cb.equal(root.get("course_type").get("id"), courseTypeId);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<Course> query = em.createQuery(cq);
		List<Course> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<Course>();
		}
	}

	public Course findByName(String courseName) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Course> cq = cb.createQuery(Course.class);
		Root<Course> root = cq.from(Course.class);
		List<Predicate> queries = new ArrayList<>();
		if (courseName != null) {
			Predicate foodNameQuery = cb.equal(root.get("name"), courseName);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<Course> query = em.createQuery(cq);
		List<Course> results = query.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return new Course();
		}
	}

	public List<Course> findByListId(List<Long> ids) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Course> cq = cb.createQuery(Course.class);
		Root<Course> root = cq.from(Course.class);
		List<Predicate> queries = new ArrayList<>();
		if (ids != null && !ids.isEmpty()) {
			queries.add(cb.in(root.get("id")).value(ids));
		} else {
			return new ArrayList<Course>();
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<Course> query = em.createQuery(cq);
		List<Course> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<Course>();
		}
	}
}
