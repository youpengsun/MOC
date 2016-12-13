package com.sap.moc.vo;

import java.util.ArrayList;

public class WechatUserList {
	private String errcode;
	private String errmsg;
	private ArrayList<WechatUser> userlist;
	
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public ArrayList<WechatUser> getUserlist() {
		return userlist;
	}
	public void setUserlist(ArrayList<WechatUser> userlist) {
		this.userlist = userlist;
	}
	
	
}
