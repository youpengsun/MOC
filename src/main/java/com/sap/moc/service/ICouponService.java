package com.sap.moc.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.moc.entity.CouponRecord;
import com.sap.moc.vo.CouponInquiryCriteria;
import com.sap.moc.vo.CouponInquiryCriteriaBuild;
import com.sap.moc.vo.CouponRecordVO;

public interface ICouponService {

	public CouponInquiryCriteriaBuild buildInquiryCriteria();

	public List<CouponRecordVO> inquiryByCriteria(CouponInquiryCriteria criteria) throws ParseException;

	public boolean saveCoupon(CouponRecord coupon);
	
	public boolean saveCoupon(List<CouponRecord> cpList);

	public boolean saveMultipleCoupon(CouponRecordVO couponVO);

	public Set<String> parseMultipleEmployeeID(CouponRecordVO couponVO);

	public String validateCouponBeforeSave(CouponRecordVO couponVO, List<Object> messages) throws ParseException;

	public Map<String, String> countTotal(List<CouponRecordVO> list);

	public List<CouponRecordVO> getTodayRecordsByEmployee(String employeeId);
}
