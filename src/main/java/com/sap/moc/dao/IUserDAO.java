package com.sap.moc.dao;

import com.sap.moc.entity.User;

public interface IUserDAO extends IGenericDAO<User, String> {
	
	public User findByUsername(String username);
}