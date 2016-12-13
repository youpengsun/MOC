package com.sap.moc.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.utils.CommonUtil;

public class SettlementRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5099768605118056072L;

	private String id;
	
	private String employeeId;
	
	private String employeeName;

	private String vendorName;

	private int vendorLineNo;

	private String vendorLineName;

	private Timestamp time;

	private String transactionCode;

	private String category;

	private String status;

	private String comment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public int getVendorLineNo() {
		return vendorLineNo;
	}

	public void setVendorLineNo(int vendorLineNo) {
		this.vendorLineNo = vendorLineNo;
	}

	public String getVendorLineName() {
		return vendorLineName;
	}

	public void setVendorLineName(String vendorLineName) {
		this.vendorLineName = vendorLineName;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SettlementRecord(ConsumeRecord record) {
		this.category = record.getCategory();
		this.comment = record.getComment();
		CommonUtil.formatEmployeeName(record.getEmployee());
		this.employeeId = record.getEmployee().getId();
		this.employeeName = record.getEmployee().getFormattedName();
		this.id = record.getId();
		this.status = record.getStatus();
		this.time = record.getTime();
		this.transactionCode = record.getTransactionCode();
		if(record.getVendorLine() != null)
		{
			VendorLine line = record.getVendorLine();
			this.vendorLineName = line.getName();
			this.vendorLineNo = line.getLineNO();
			this.vendorName = line.getVendor().getName();
			}
	}

}
