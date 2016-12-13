package com.sap.moc.vo;

import java.util.ArrayList;

public class WechatDepartmentList {
	private String errcode;
	private String errmsg;
	private ArrayList<WechatDepartment> department;
	
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
	public ArrayList<WechatDepartment> getDepartment() {
		return department;
	}
	public void setDepartment(ArrayList<WechatDepartment> department) {
		this.department = department;
	}
		
}
