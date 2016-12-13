package com.sap.moc.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sap.moc.entity.VendorLine;
import com.sap.moc.utils.ConstantReader;

public class SettlementLine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1992843316954589205L;

	private int id;

	private int lineNO;

	private String lineName;

	private String wechatID;

	private int consumeCount;

	private float consumeTotal;

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

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getWechatID() {
		return wechatID;
	}

	public void setWechatID(String wechatID) {
		this.wechatID = wechatID;
	}

	private List<SettlementRecord> detailRecords;

	public List<SettlementRecord> getDetailRecords() {
		if (this.detailRecords == null) {
			this.detailRecords = new ArrayList<SettlementRecord>();
		}
		return detailRecords;
	}

	public void setDetailRecords(List<SettlementRecord> detailRecords) {
		this.detailRecords = detailRecords;
	}

	public int getConsumeCount() {
		return consumeCount;
	}

	public float getConsumeTotal() {
		return consumeTotal;
	}

	public SettlementLine(VendorLine line) {
		this.lineNO = line.getLineNO();
		this.id = line.getId();
		this.lineName = line.getName();
		this.wechatID = line.getWechatID();
	}

	public void aggregate() {
		this.consumeCount = detailRecords.size();
		int price = Integer.parseInt(ConstantReader.readByKey("MEAL_PRICE"));
		this.consumeTotal = consumeCount * price;
	}

}
