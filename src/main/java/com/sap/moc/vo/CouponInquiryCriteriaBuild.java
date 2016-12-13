package com.sap.moc.vo;

import java.util.Date;
import java.util.List;

public class CouponInquiryCriteriaBuild {
	private String dateFormat;
	private Date begda;
	private Date endda;
	private List costCenterList;
	private List typeList;
	
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
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
	public List getCostCenterList() {
		return costCenterList;
	}
	public void setCostCenterList(List costCenter) {
		this.costCenterList = costCenter;
	}
	public List getTypeList() {
		return typeList;
	}
	public void setTypeList(List typeList) {
		this.typeList = typeList;
	}
}
