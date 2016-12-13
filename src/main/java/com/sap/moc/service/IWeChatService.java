package com.sap.moc.service;

import javax.servlet.http.HttpServletRequest;

public interface IWeChatService {
	
	public String processRequest(String msg,HttpServletRequest request);

}
