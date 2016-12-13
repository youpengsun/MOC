package com.sap.moc.vo;

import java.util.Date;
import java.util.List;

import com.sap.moc.entity.Vendor;

@SuppressWarnings("rawtypes")
public class ConsumeInquiryCriteriaBuild {
	
	private String dateFormat;
	private Date begda;
	private Date endda;
	private List<Vendor> vendor;
	private List costCenter;
	private List category;
	private List status;
	
	public List<Vendor> getVendor() {
		return vendor;
	}
	public void setVendor(List<Vendor> vendor) {
		this.vendor = vendor;
	}
	public List getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(List costCenter) {
		this.costCenter = costCenter; 
	}
	public List getCategory() {
		return category;
	}
	public void setCategory(List category) {
		this.category = category;
	}
	public List getStatus() {
		return status;
	}
	public void setStatus(List status) {
		this.status = status;
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
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFromat) {
		this.dateFormat = dateFromat;
	}
	
	
	
}
