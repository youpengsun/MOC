package com.sap.moc.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.sap.moc.dao.IGenericDAO;
import com.sap.moc.vo.Pager;

@Repository
@SuppressWarnings("unchecked")
public abstract class GenericDAOImpl<E, K extends Serializable> implements IGenericDAO<E, K> {
	protected Class<?> entityClass;

	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public GenericDAOImpl() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		this.entityClass = (Class<?>) pt.getActualTypeArguments()[0];
	}

	protected Session currentSession() {
	/*	Session session = sessionFactory.getCurrentSession();
		session.setFlushMode(FlushMode.AUTO);
		return session;*/
		return sessionFactory.getCurrentSession();
	}

	public boolean create(E entity) {
		try {
			currentSession().save(entity);
			return true;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public boolean saveOrUpdate(E entity) {
		try {
			currentSession().saveOrUpdate(entity);
			//currentSession().flush();
			
			return true;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public boolean update(E entity) {
		try {
			currentSession().update(entity);
			return true;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public E findByKey(K key) {
		return (E) currentSession().get(entityClass, key);
	}

	public List<E> findByProperty(String propertyName, Object value) {
		Assert.hasText(propertyName);
		List<E> result = currentSession().createCriteria(entityClass).add(Restrictions.eq(propertyName, value)).list();
		return result;
	}

	public List<E> findByProperty(Map<String, Object> conditionMap) {
		StringBuilder query = new StringBuilder();
		query.append("from  " + entityClass.getSimpleName());
		if (!conditionMap.isEmpty()) {
			Iterator<String> it = conditionMap.keySet().iterator();
			String key = it.next();
			query.append(" where  " + key + "=:" + conditionMap.get(key));
			while (it.hasNext()) {
				key = it.next();
				query.append(" and  " + key + "=:" + conditionMap.get(key));
			}
		}
		return currentSession().createQuery(query.toString()).list();
	}

	public List<E> getAll() {
		return currentSession().createCriteria(entityClass).list();
	}

	public boolean saveOrUpdateAll(Collection<E> entities) {
		try {
			for (@SuppressWarnings("rawtypes")
			Iterator localIterator = entities.iterator(); localIterator.hasNext();) {
				Object entity = localIterator.next();
				currentSession().saveOrUpdate(entity);
			}
			return true;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public boolean delete(E entity) {

		try {
			currentSession().delete(entity);
			return true;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<E> searchBySQL(String sql) {
		List<E> enties = currentSession().createSQLQuery(sql).list();
		return enties;
	}

	public List<E> searchByCriteria(List<Criterion> criterions, List<Order> orders) {
		Criteria criteria = buildCriteria(criterions, orders);
		return criteria.list();
	}

	public List<E> searchByHQL(String hql, Map<String, Object> properties) {
		Query query = buildQuery(hql, properties);
		return query.list();
	}

	public Pager<E> searchByCriteria(int currentPage, int pageLine, List<Criterion> criterions, List<Order> orders) {
		Criteria criteria = buildCriteria(criterions, orders);

		// Pagination
		criteria.setFirstResult((currentPage - 1) * pageLine);
		criteria.setMaxResults(pageLine);

		List<E> entryList = criteria.list();
		int pageCount = (int) Math.ceil(entryList.size() / pageLine);

		// Set Pager
		Pager<E> pager = new Pager<E>(pageLine, pageCount);
		pager.setCurrentPage(currentPage);
		pager.setEntryList(entryList);
		return pager;
	}

	public Pager<E> searchByHQL(int currentPage, int pageLine, String hql, Map<String, Object> properties) {
		Query query = buildQuery(hql, properties);

		query.setFirstResult((currentPage - 1) * pageLine);
		query.setMaxResults(pageLine);

		List<E> entryList = query.list();
		int pageCount = (int) Math.ceil(entryList.size() / pageLine);

		// Set Pager
		Pager<E> pager = new Pager<E>(pageLine, pageCount);
		pager.setCurrentPage(currentPage);
		pager.setEntryList(entryList);
		return pager;
	}

	private Criteria buildCriteria(List<Criterion> criterions, List<Order> orders) {
		Criteria criteria = currentSession().createCriteria(entityClass);

		// Criterion List
		if (criterions.size() > 0) {
			for (Criterion criterion : criterions) {
				if (criterion != null) {
					criteria.add(criterion);
				}
			}
		}

		// Order List
		if (orders.size() > 0) {
			for (Order order : orders) {
				if (order != null) {
					criteria.addOrder(order);
				}
			}
		}
		return criteria;
	}

	private Query buildQuery(String hql, Map<String, Object> properties) {
		Query query = currentSession().createQuery(hql);
		if (properties.size() > 0) {
			query.setProperties(properties);
		}
		return query;

	}

}
