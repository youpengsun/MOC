package com.sap.moc.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "VENDOR_LINE", uniqueConstraints = {@UniqueConstraint(columnNames={"VENDOR_ID", "LINE_NO"})} )
public class VendorLine {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID",length = 8)
	private int id;

	@Column(name = "LINE_NO")
	private int lineNO;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_ID")
	private Vendor vendor;
	
	@Column(name = "NAME",length = 45)
	private String name;
	
	@Column(name = "WECHAT_ID", length = 40)
	private String wechatID;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLineNO() {
		return lineNO;
	}

	public void setLineNO(int lineNO) {
		this.lineNO = lineNO;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWechatID() {
		return wechatID;
	}

	public void setWechatID(String wechatID) {
		this.wechatID = wechatID;
	}
	
	
}
