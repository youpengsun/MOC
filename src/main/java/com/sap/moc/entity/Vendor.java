package com.sap.moc.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "VENDOR")
@JsonIgnoreProperties("vendorLineList")
public class Vendor {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", length = 8)
	private int id;

	@Column(name = "NAME", length = 60)
	private String name;

	@ManyToOne
	@JoinColumn(name = "BUSINESS_DISTRICT")
	private District businessDistrict;

	@Column(name = "CONTACT_NAME", length = 60)
	private String contactName;

	@Column(name = "CONTACT_TEL_NO", length = 30)
	private String contactTelNO;

	@Column(name = "CONTACT_EMAIL", length = 60)
	private String contactEmail;

	@Column(name = "ADDRESS", length = 100)
	private String address;

	@Column(name = "WECHAT_ID", length = 40)
	private String wechatID;

	@Column(name = "DIANPING_ID", length = 10)
	private String dianpingID;

	@OneToMany(mappedBy = "vendor")
	private List<VendorLine> vendorLineList;

	@OneToMany(mappedBy = "vendor")
	@JsonIgnore
	@OrderBy(clause = "endDate desc")
	private List<Contract> contractList;

	@Column(name = "REPORT_PERIOD", length = 2)
	private String reportPeriod;

	@Column(name = "VENDOR_TYPE", length = 2)
	private String vendorType;

	@Column(name = "PROMOTION", length = 100)
	private String promotion; 
	
	@Transient
	private String activeStatus;
	

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

	public District getBusinessDistrict() {
		return businessDistrict;
	}

	public void setBusinessDistrict(District businessDistrict) {
		this.businessDistrict = businessDistrict;
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

	public List<VendorLine> getVendorLineList() {
		return vendorLineList;
	}

	public void setVendorLineList(List<VendorLine> vendorLineList) {
		this.vendorLineList = vendorLineList;
	}

	public List<Contract> getContractList() {
		return contractList;
	}

	public void setContractList(List<Contract> contractList) {
		this.contractList = contractList;
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

	public String getActiveStatus() {
		if (activeStatus == null) {
			activeStatus = "";
		}
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

}
