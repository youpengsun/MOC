package com.sap.moc.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.vo.ConsumeInquiryCriteria;
import com.sap.moc.vo.ConsumeInquiryCriteriaBuild;
import com.sap.moc.vo.ConsumeRecordVO;
import com.sap.moc.vo.ConsumeSaveResult;
import com.sap.moc.vo.QueryTime;
import com.sap.moc.vo.SettlementReport;

public interface IConsumeService {

	public ConsumeRecord getConsumeRecord(String id);

	public ConsumeSaveResult saveConsumeRecord(String employeeId, String scanCode);

	public List<ConsumeRecord> getTodayConsumeRecord(String employeeId, String status, String category);

	public SettlementReport getSettlementReport(int vendorID, QueryTime time);
	
	public SettlementReport getSettlementReport(int vendorID, Date from, Date to);

	public List<ConsumeRecordVO> getRecordsByVendorPeriod(VendorLine line, String status, Timestamp from, Timestamp to);

	public ConsumeInquiryCriteriaBuild buildInquiryCriteria();

	public List<ConsumeRecordVO> inquiryByCriteria(ConsumeInquiryCriteria criteria) throws ParseException;

	public int getVendorLineTodayActiveCount(int vendorLine);

	public byte[] buildExcelOfMonthlySettlement(SettlementReport settlement);
}