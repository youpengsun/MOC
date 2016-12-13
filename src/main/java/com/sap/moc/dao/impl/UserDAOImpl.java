package com.sap.moc.dao.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.sap.moc.dao.IUserDAO;
import com.sap.moc.entity.User;

@Repository
public class UserDAOImpl extends GenericDAOImpl<User, String> implements IUserDAO {

	@Override
	public User findByUsername(String username) {

		StringBuilder hql = new StringBuilder();
		hql.append("from User u where u.username = :username");

		Query query = currentSession().createQuery(hql.toString());
		query.setParameter("username", username);

		return  (User)query.uniqueResult();
	}
	
}