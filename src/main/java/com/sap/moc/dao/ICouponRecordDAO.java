package com.sap.moc.dao;

import java.text.ParseException;
import java.util.List;

import com.sap.moc.entity.CouponRecord;
import com.sap.moc.vo.CouponInquiryCriteria;

public interface ICouponRecordDAO extends IGenericDAO<CouponRecord, String>{

	public List<CouponRecord> findByInquiryCriteria(CouponInquiryCriteria criteria) throws ParseException;
}
