package com.sap.moc.service;

import java.util.Map;

import com.sap.moc.entity.User;
import com.sap.moc.vo.ResetPassword;

public interface IUserService {
	
	public User getUser (String username);
	
	public boolean validateUser(User user, String password);
	
	public boolean resetPassword(User user);
	
	public Map<String, String> resetPassword(String username, ResetPassword reset);
	
}
