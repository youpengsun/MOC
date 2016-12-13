package com.sap.moc.service.impl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailPasswordAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public EmailPasswordAuthenticator(){ 
	    }

	public EmailPasswordAuthenticator(String username, String password) {  
	        this.userName = username;  
	        this.password = password;  
	    }

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}

}
