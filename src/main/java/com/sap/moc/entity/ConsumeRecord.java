package com.sap.moc.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sap.moc.utils.CommonUtil;

@Entity
@Table(name = "CONSUME_RECORD")
public class ConsumeRecord {

	@Id
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@GeneratedValue(generator = "idGenerator")
	@Column(name = "ID", length = 32)
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYEE_ID")
	private Employee employee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_LINE_ID")
	private VendorLine vendorLine;

	@Column(name = "TIME")
	private Timestamp time;

	@Column(name = "TRANSACTION_CODE", length = 10)
	private String transactionCode;

	@Column(name = "CATEGORY", length = 2)
	private String category;

	@Column(name = "STATUS", length = 2)
	private String status;

	@Column(name = "COMMENT", length = 100)
	private String comment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public VendorLine getVendorLine() {
		return vendorLine;
	}

	public void setVendorLine(VendorLine vendorLine) {
		this.vendorLine = vendorLine;
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

	public ConsumeRecord(Employee employee, VendorLine vendorLine) {
		this.employee = employee;
		this.vendorLine = vendorLine;
		// Set Current time
		this.time = CommonUtil.getCurrentTimestamp();
	}

	public ConsumeRecord() {

	}
}
