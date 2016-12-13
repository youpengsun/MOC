package com.sap.moc.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.moc.dao.IUserDAO;
import com.sap.moc.entity.User;
import com.sap.moc.security.PassGenerate;
import com.sap.moc.security.PassProtect;
import com.sap.moc.service.IUserService;
import com.sap.moc.vo.ResetPassword;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDAO userDAO;

	@Override
	public boolean validateUser(User user, String password) {

		if (user == null || password == null) {
			return false;
		} else {
			return PassProtect.verifyPasswordSaltHash(password, user.getSalt(), user.getPassword());

		}
	}

	@Override
	public User getUser(String username) {

		User user = userDAO.findByUsername(username);
		
		return user;
	}

	@Override
	public boolean resetPassword(User loginUser) {
		User user  = userDAO.findByUsername(loginUser.getUsername());
		user.setSalt(PassGenerate.generateSalt().substring(0, 5)); 
		
		String passwordHash = PassProtect.hashPasswordSalt(loginUser.getPassword(), user.getSalt());
		user.setPassword(passwordHash);
		return userDAO.update(user);
	}

	@Override
	public Map<String, String> resetPassword(String username, ResetPassword reset) {
		Map<String, String> map = new HashMap<String, String>();
		User user = userDAO.findByUsername(username);
		if(user == null){
			map.put("result","false");
			map.put("error", "login");
		}
		else{
			boolean status = validateUser(user, reset.getOldPassword());
			if(status){
				user.setSalt(PassGenerate.generateSalt().substring(0, 5)); 
				String passwordHash = PassProtect.hashPasswordSalt(reset.getNewPassword(), user.getSalt());
				user.setPassword(passwordHash);
				status= userDAO.update(user);
				if(status){
					map.put("result","true");
				}
				else{
					map.put("result","false");
					map.put("error", "save");
				}
			}
			else{
				map.put("result", "false");
				map.put("error", "password");
			}
		}
		return map;
		
	}


	
	

}
