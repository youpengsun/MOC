package com.sap.moc.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.sap.moc.utils.CommonUtil;

public class ConsumeInquiryCriteria {
	private Date begda;
	private Date endda;
	private String costerCenterID;
	private String vendorID;
	private String categoryKey;
	private String statusKey;
	private String employeeID;
	private String firstName;
	private String lastName;
	private String transactionCode;
	public String getCosterCenterID() {
		return costerCenterID;
	}
	public void setCosterCenterID(String costerCenterID) {
		this.costerCenterID = costerCenterID;
	}
	public String getVendorID() {
		return vendorID;
	}
	public void setVendorID(String vendorID) {
		this.vendorID = vendorID;
	}
	public String getCategoryKey() {
		return categoryKey;
	}
	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}
	public String getStatusKey() {
		return statusKey;
	}
	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}
	public String getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public Date getBegda() {
		return begda;
	}
	public void setBegda(Date begda) {
		this.begda = begda;
	}
	public Date getEndda() {
		return endda;
	}
	public void setEndda(Date endda) {
		this.endda = endda;
	}
	
	public void initializeBegdaTime() throws ParseException{
		
		if (this.begda == null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			this.begda = df.parse("19000101");
		}
		
		Calendar cal = CommonUtil.getCurrentCalendar();
		cal.setTime(this.begda);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		this.begda = cal.getTime();
	}
	public void initializeEnddaTime() throws ParseException{
		if (this.endda == null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			this.endda = df.parse("99991231");
		}
		
		Calendar cal = CommonUtil.getCurrentCalendar();
		cal.setTime(this.endda);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		this.endda = cal.getTime();
	}
	
}
