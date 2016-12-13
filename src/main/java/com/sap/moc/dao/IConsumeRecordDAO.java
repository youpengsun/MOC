package com.sap.moc.dao;

import java.sql.Timestamp;
import java.text.ParseException;
import java.sql.Date;
import java.util.List;

import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.vo.ConsumeInquiryCriteria;
import com.sap.moc.vo.QueryTime;

public interface IConsumeRecordDAO extends IGenericDAO<ConsumeRecord, String> {

	public List<ConsumeRecord> getTodayConsumeRecord(String employeeId, String status, String category);

	public List<ConsumeRecord> getConsumeRecordsByVendor(int vendorID, QueryTime time);

	public List<ConsumeRecord> getConsumeRecordsByVendor(int vendorID, Date beginDate, Date endDate);
	
	public List<ConsumeRecord> getRecordsByVendorPeriod(int vendor_line_id, String status, Timestamp from,
			Timestamp to);

	public List<ConsumeRecord> findByInquiryCriteria(ConsumeInquiryCriteria criteria) throws ParseException;

	@SuppressWarnings("rawtypes")
	public List getAnalysisResource(QueryTime begmo, QueryTime endmo) throws ParseException;
	
	public int getVendorLineCountByDate(int vendorLineID, java.util.Date begda, java.util.Date endda, String status);
	
}
