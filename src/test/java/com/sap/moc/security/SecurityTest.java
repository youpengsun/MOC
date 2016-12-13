package com.sap.moc.security;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.sap.moc.dao.IUserDAO;
import com.sap.moc.entity.User;
import com.sap.moc.service.IUserService;
import com.sap.moc.test.BaseJunit4Test;

public class SecurityTest extends BaseJunit4Test{
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IUserDAO userDAO;
	
//	@Test
//	public void testPasswordSalt()
//	{
//		String password = "111111";
//		String salt = "111111";
//		
//		String passwordSaltHash = PassProtect.hashPasswordSalt(password, salt);
//		
//		System.out.println(passwordSaltHash);
//		
//		assertEquals(passwordSaltHash, "9cOsHPCWCU0YSJnfo40srutIZjiF5EfmGTvOgC8ZKtc=");
//		
//	}
//	
	@Test
	@Rollback(false)
	public void testresetPassword()
	{
		System.out.println("begin");
		User user = userService.getUser("admin");
		String password = "111111";
		String salt = "111111";
		
		String passwordSaltHash = PassProtect.hashPasswordSalt(password, salt);
		user.setSalt(salt);
		user.setPassword(passwordSaltHash);
		System.out.println(passwordSaltHash);
		System.out.println(userDAO.saveOrUpdate(user));
		
		
		
	}
	
}
