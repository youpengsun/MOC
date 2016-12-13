package com.sap.moc.service;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.sap.moc.entity.User;
import com.sap.moc.test.BaseJunit4Test;

public class UserServiceTest extends BaseJunit4Test {
        
    @Autowired
    private IUserService userService;

	@Test
	@Rollback(false)
	public void testresetPassword()
	{
		System.out.println("begin");
		User user = userService.getUser("admin");
//		user.setPassword("111111");
//		userService.resetPassword(user);
		System.out.println(userService.validateUser(user, "111111"));
		//user.setSalt("111111");//user.setSalt(PassGenerate.generateSalt()); 
		//user.setPassword(PassProtect.hashPasswordSalt(user.getPassword(), user.getSalt()));
		//userDAO.saveOrUpdate(user);
//		String password = "111111";
//		String salt = "111111";
//		
//		String passwordSaltHash = PassProtect.hashPasswordSalt(password, salt);
//		user.setSalt(salt);
//		user.setPassword(passwordSaltHash);
//		System.out.println(passwordSaltHash);
//		System.out.println(userDAO.saveOrUpdate(user));
		
		
		
	}

}
