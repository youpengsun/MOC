package com.sap.moc.dao.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.moc.dao.IUserDAO;
import com.sap.moc.entity.User;
import com.sap.moc.test.BaseJunit4Test;

public class UserDAOImplTest extends BaseJunit4Test {

	@Autowired
	private IUserDAO userDAO;
	
	@Test
	public void testFindByUsername() {
		
		User user = userDAO.findByUsername("administrator");
		
		System.out.println(user.getUsername());
		assertNotNull(user);
	}

}
