package com.sap.moc.vo;

import java.sql.Date;

public class SettlementRequest {
	private QueryTime time;

	private int vendorId;
	
	private Date beginDate;
	
	private Date endDate;
	
	public QueryTime getTime() {
		return time;
	}

	public void setTime(QueryTime time) {
		this.time = time;
	}

	public int getVendorId() {
		return vendorId;
	}

	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
