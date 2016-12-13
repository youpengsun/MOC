package com.sap.moc.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sap.moc.service.ICouponService;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.vo.CouponInquiryCriteria;
import com.sap.moc.vo.CouponInquiryCriteriaBuild;
import com.sap.moc.vo.CouponRecordVO;

@Controller
public class CouponController {

	@Autowired
	private ICouponService couponService;

	@RequestMapping("/coupon/buildInquiryCriteria")
	@ResponseBody
	public Map<String, Object> buildCriteria() {

		Map<String, Object> map = new HashMap<String, Object>();

		CouponInquiryCriteriaBuild criteriaBuild;
		criteriaBuild = couponService.buildInquiryCriteria();

		map.put("criteriaBuild", criteriaBuild);
		return map;
	}

	@RequestMapping("/coupon/inquiry")
	@ResponseBody
	public Map<String, Object> couponInquiry(@RequestBody CouponInquiryCriteria criteria) throws ParseException {

		Map<String, Object> map = new HashMap<String, Object>();
		List<CouponRecordVO> list = couponService.inquiryByCriteria(criteria);

		Map<String, String> countTotal = couponService.countTotal(list);
		map.put("couponRecords", list);
		map.put("countTotal", countTotal);
		return map;
	}

	@RequestMapping("/coupon/save/{flag}")
	@ResponseBody
	public Map<String, Object> addNewCoupon(@RequestBody CouponRecordVO couponVO, @PathVariable String flag)
			throws ParseException {

		/*
		 * Flag indicate the following options:
		 * 
		 * 1: for the first time when submit the save coupon request 2:
		 * validation status 'Warning' occurred, but user still proceeds
		 * forcibly
		 * 
		 */

		Map<String, Object> map = new HashMap<String, Object>();

		// Parse the EE ID list for multiple input
		couponVO.setEmployeeList(couponService.parseMultipleEmployeeID(couponVO));

		// validate the new coupon records before save
		String status = "";
		List<Object> validateMessages = new ArrayList<>();
		status = couponService.validateCouponBeforeSave(couponVO, validateMessages);
		if (status.equals(ConstantUtil.COUPON_VALIDATE_STATUS_FAILED)) {

			map.put("status", ConstantUtil.COUPON_VALIDATE_STATUS_FAILED);
			map.put("messages", validateMessages);

		} else if (status.equals(ConstantUtil.COUPON_VALIDATE_STATUS_WARNING) && !flag.equals("2")) {

			map.put("status", ConstantUtil.COUPON_VALIDATE_STATUS_WARNING);
			map.put("messages", validateMessages);

		} else if (status.equals(ConstantUtil.COUPON_VALIDATE_STATUS_SUCCESS)
				|| (status.equals(ConstantUtil.COUPON_VALIDATE_STATUS_WARNING) && "2".equals(flag))) {

			boolean saveStatus = couponService.saveMultipleCoupon(couponVO);
			if (saveStatus) {
				map.put("status", ConstantUtil.COUPON_SAVE_STATUS_SUCCESS);
			} else {
				map.put("status", ConstantUtil.COUPON_SAVE_STATUS_FAILED);
			}
		}

		return map;
	}

}
