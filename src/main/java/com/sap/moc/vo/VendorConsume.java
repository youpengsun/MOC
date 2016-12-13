package com.sap.moc.vo;

import java.io.Serializable;
import java.util.Map;

public class VendorConsume implements Serializable {

	private static final long serialVersionUID = 2161962022903606661L;

	private int id;

	private String name;

	private String vendorType;
	
	private String activeStatus;

	private Integer[] data;

	private Map<String, Integer> dataList;

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

	public Integer[] getData() {
		data = dataList.values().toArray(new Integer[dataList.size()]);
		return data;
	}

	public Map<String, Integer> getDataList() {
		return dataList;
	}

	public void setDataList(Map<String, Integer> dataList) {
		this.dataList = dataList;
	}

	public String getVendorType() {
		return vendorType;
	}

	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	public VendorConsume() {
		super();
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}



}
