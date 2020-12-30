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
import trong.lixco.com.jpa.entities.QuestionType;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class QuestionTypeService extends AbstractService<QuestionType> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<QuestionType> getEntityClass() {
		return QuestionType.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	public List<QuestionType> findByName(String foodName) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QuestionType> cq = cb.createQuery(QuestionType.class);
		Root<QuestionType> root = cq.from(QuestionType.class);
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
		TypedQuery<QuestionType> query = em.createQuery(cq);
		List<QuestionType> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<QuestionType>();
		}
	}

	public List<QuestionType> findAllNew() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<QuestionType> cq = cb.createQuery(QuestionType.class);
		Root<QuestionType> root = cq.from(QuestionType.class);
		cq.select(root);
		TypedQuery<QuestionType> query = em.createQuery(cq);
		List<QuestionType> results = query.getResultList();
		if (!results.isEmpty()) {
			return results;
		} else {
			return new ArrayList<QuestionType>();
		}
	}
}
