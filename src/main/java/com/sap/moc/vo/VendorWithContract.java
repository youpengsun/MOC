package com.sap.moc.vo;

import java.sql.Date;

public class VendorWithContract {
	
	private int id;

	private String name;
	
	private String contactName;

	private String contactTelNO;
	
	private String businessDistrictId;
	
	private String contactEmail;

	private String address;

	private String wechatID;
	
	private String promotion;

	private String dianpingID;

	private String reportPeriod;

	private String vendorType;
	
    private int contractId;
    
    private Date beginDate;
    
    private Date endDate;
    
    private String comment;
    
    private String contract_no;
    
    private String status;
    

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTelNO() {
		return contactTelNO;
	}

	public void setContactTelNO(String contactTelNO) {
		this.contactTelNO = contactTelNO;
	}

	public String getBusinessDistrictId() {
		return businessDistrictId;
	}

	public void setBusinessDistrictId(String businessDistrictId) {
		this.businessDistrictId = businessDistrictId;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWechatID() {
		return wechatID;
	}

	public void setWechatID(String wechatID) {
		this.wechatID = wechatID;
	}

	public String getDianpingID() {
		return dianpingID;
	}

	public void setDianpingID(String dianpingID) {
		this.dianpingID = dianpingID;
	}

	public String getReportPeriod() {
		return reportPeriod;
	}

	public void setReportPeriod(String reportPeriod) {
		this.reportPeriod = reportPeriod;
	}

	public String getVendorType() {
		return vendorType;
	}

	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getContract_no() {
		return contract_no;
	}

	public void setContract_no(String contract_no) {
		this.contract_no = contract_no;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	
//    private Vendor vendor;
//    private Contract contract;
//    
//	public Vendor getVendor() {
//		return vendor;
//	}
//	public void setVendor(Vendor vendor) {
//		this.vendor = vendor;
//	}
//	public Contract getContract() {
//		return contract;
//	}
//	public void setContract(Contract contract) {
//		this.contract = contract;
//	}


}
