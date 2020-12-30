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
import trong.lixco.com.jpa.entities.Skill;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SkillService extends AbstractService<Skill> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<Skill> getEntityClass() {
		return Skill.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	public List<Skill> findByCourse(long courseId) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Skill> cq = cb.createQuery(Skill.class);
		Root<Skill> root = cq.from(Skill.class);
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
		TypedQuery<Skill> query = em.createQuery(cq);
		List<Skill> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<Skill>();
		}
	}

	public Skill findByName(String name) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Skill> cq = cb.createQuery(Skill.class);
		Root<Skill> root = cq.from(Skill.class);
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
		TypedQuery<Skill> query = em.createQuery(cq);
		List<Skill> results = query.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return new Skill();
		}
	}
}
