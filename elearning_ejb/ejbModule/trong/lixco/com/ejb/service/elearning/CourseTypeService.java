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
import trong.lixco.com.jpa.entities.CourseType;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CourseTypeService extends AbstractService<CourseType> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<CourseType> getEntityClass() {
		return CourseType.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	public List<CourseType> findByName(String foodName) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CourseType> cq = cb.createQuery(CourseType.class);
		Root<CourseType> root = cq.from(CourseType.class);
		List<Predicate> queries = new ArrayList<>();
		if (foodName != null) {
			Predicate foodNameQuery = cb.equal(root.get("name"), foodName);
			queries.add(foodNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<CourseType> query = em.createQuery(cq);
		List<CourseType> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<CourseType>();
		}
	}

	public List<CourseType> findAllNew() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<CourseType> cq = cb.createQuery(CourseType.class);
		Root<CourseType> root = cq.from(CourseType.class);
		cq.select(root);
		TypedQuery<CourseType> query = em.createQuery(cq);
		List<CourseType> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<CourseType>();
		}
	}
}
