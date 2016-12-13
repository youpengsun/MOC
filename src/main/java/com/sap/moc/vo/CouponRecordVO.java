package com.sap.moc.vo;

import java.util.Date;
import java.util.Set;

public class CouponRecordVO {

	private Set<String> employeeList;
	private String employeeID;
	private String employeeName;
	private String costCenterID;
	private String costCenterName;
	private String type;
	private String typeText;
	private String count;
	private Date regDate;
	private String regDateText;
	private String comment;
	private String misuseType;
	private String misuseTypeText;
	

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getCostCenterID() {
		return costCenterID;
	}

	public void setCostCenterID(String costCenterID) {
		this.costCenterID = costCenterID;
	}

	public String getCostCenterName() {
		return costCenterName;
	}

	public void setCostCenterName(String costCenterName) {
		this.costCenterName = costCenterName;
	}

	public String getTypeText() {
		return typeText;
	}

	public void setTypeText(String typeText) {
		this.typeText = typeText;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<String> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(Set<String> employeeList) {
		this.employeeList = employeeList;
	}

	public String getRegDateText() {
		return regDateText;
	}

	public void setRegDateText(String regDateText) {
		this.regDateText = regDateText;
	}

	public String getMisuseType() {
		return misuseType;
	}

	public void setMisuseType(String misuseType) {
		this.misuseType = misuseType;
	}

	public String getMisuseTypeText() {
		return misuseTypeText;
	}

	public void setMisuseTypeText(String misuseTypeText) {
		this.misuseTypeText = misuseTypeText;
	}
	
	

}
