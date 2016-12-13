package com.sap.moc.vo;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.entity.Vendor;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.utils.ConstantReader;

public class SettlementReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1162649920558434403L;

	private List<SettlementLine> lineRecords;

	private float totalAmount;

	private float totalCount;

	private QueryTime time;

	private Date beginDate;

	private Date endDate;

	private String vendorName;

	private String vendorAddress;

	private int vendorId;

	private String vendorWechat;

	private String vendorEmail;

	private String contractName;

	private String telephone;

	private int mealPrice;

	private String companyName;

	private String companyAddress;

	private String companyContact;

	private String companyContactTel;

	public SettlementReport() {

		this.mealPrice = CommonUtil.getMealPrice();
		this.companyAddress = ConstantReader.readByKey("SAP_ADDRESS");
		this.companyContact = ConstantReader.readByKey("SAP_CONTACT");
		this.companyContactTel = ConstantReader.readByKey("SAP_CONTACT_TEL");
		this.companyName = ConstantReader.readByKey("SAP_COMPANY_NAME");
	}

	public String getVendorEmail() {
		return vendorEmail;
	}

	public int getVendorId() {
		return vendorId;
	}

	public String getVendorWechat() {
		return vendorWechat;
	}

	private String comment;

	public List<SettlementLine> getLineRecords() {
		return lineRecords;
	}

	public QueryTime getTime() {
		return time;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setTotalCount(float totalCount) {
		this.totalCount = totalCount;
	}

	public void setLineRecords(List<SettlementLine> lineRecords) {
		this.lineRecords = lineRecords;
	}

	public float getTotalAmount() {
		return totalAmount;
	}

	public String getVendorAddress() {
		return vendorAddress;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public float getTotalCount() {
		return totalCount;
	}

	public QueryTime getQueryTime() {
		return time;
	};

	public String getVendorName() {
		return vendorName;
	}

	public String getContractName() {
		return contractName;
	}

	public String getTelephone() {
		return telephone;
	}

	public int getMealPrice() {
		return mealPrice;
	}

	public void setMealPrice(int mealPrice) {
		this.mealPrice = mealPrice;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public String getCompanyContact() {
		return companyContact;
	}

	public String getCompanyContactTel() {
		return companyContactTel;
	}

	public void setCompanyContactTel(String companyContactTel) {
		this.companyContactTel = companyContactTel;
	}

	public SettlementReport(QueryTime time, Vendor vendor, List<ConsumeRecord> cosumeRecords) {

		this();
		this.time = time;
		this.vendorName = vendor.getName();
		this.vendorAddress = vendor.getAddress();
		this.vendorId = vendor.getId();
		this.vendorWechat = vendor.getWechatID();
		this.vendorEmail = vendor.getContactEmail();

		this.contractName = vendor.getContactName();
		this.telephone = vendor.getContactTelNO();

		buildLineRecords(cosumeRecords);
		aggregate();
	}

	public SettlementReport(Date beginDate, Date endDate, Vendor vendor, List<ConsumeRecord> cosumeRecords) {

		this(new QueryTime(), vendor, cosumeRecords);
		this.beginDate = beginDate;
		this.endDate = endDate;
	}

	private void buildLineRecords(List<ConsumeRecord> cosumeRecords) {
		Map<Integer, SettlementLine> detailMap = new HashMap<Integer, SettlementLine>();

		for (ConsumeRecord consumeRecord : cosumeRecords) {
			SettlementRecord record = new SettlementRecord(consumeRecord);
			VendorLine vLine = consumeRecord.getVendorLine();
			
			int lineId = vLine.getId();
			if (detailMap.containsKey(lineId)) {
				detailMap.get(lineId).getDetailRecords().add(record);
			} else {
				SettlementLine line = new SettlementLine(vLine);
				detailMap.put(lineId, line);

				line.getDetailRecords().add(record);
			}
		}

		this.lineRecords = new ArrayList<SettlementLine>(detailMap.values());
	}

	private void aggregate() {
		for (SettlementLine line : lineRecords) {
			line.aggregate();
			this.totalCount += line.getConsumeCount();
			this.totalAmount += line.getConsumeTotal();
		}
	}
}
