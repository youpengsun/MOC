package com.sap.moc.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.sap.moc.vo.Pager;

public interface IGenericDAO<E, K> {
	public boolean create(E entity);

	public boolean saveOrUpdate(E entity);

	public boolean update(E entity);

	public E findByKey(K key);

	public List<E> findByProperty(String name, Object value);

	public List<E> findByProperty(Map<String, Object> conditionMap);

	public List<E> getAll();

	public boolean saveOrUpdateAll(Collection<E> entities);

	public boolean delete(E entity);
	
	public List<E> searchBySQL(String sql);

	public List<E> searchByCriteria(List<Criterion> criterions, List<Order> orders);

	public List<E> searchByHQL(String hql, Map<String, Object> properties);

	public Pager<E> searchByCriteria(int currentPage, int pageLine, List<Criterion> criterions, List<Order> orders);

	public Pager<E> searchByHQL(int currentPage, int pageLine, String hql, Map<String, Object> properties);

}