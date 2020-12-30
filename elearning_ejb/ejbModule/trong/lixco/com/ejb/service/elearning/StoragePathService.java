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
import trong.lixco.com.jpa.entities.Skill;
import trong.lixco.com.jpa.entities.StoragePath;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StoragePathService extends AbstractService<StoragePath> {
	@Inject
	private EntityManager em;
	@Resource
	private SessionContext ct;

	@Override
	protected Class<StoragePath> getEntityClass() {
		return StoragePath.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	public StoragePath findByName(String name) {
		// primary
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<StoragePath> cq = cb.createQuery(StoragePath.class);
		Root<StoragePath> root = cq.from(StoragePath.class);
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
		TypedQuery<StoragePath> query = em.createQuery(cq);
		List<StoragePath> results = query.getResultList();
		if (!results.isEmpty()) {
			return results.get(0);
		} else {
			return new StoragePath();
		}
	}
}
