package com.sap.moc.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.moc.dao.ICouponRecordDAO;
import com.sap.moc.entity.CouponRecord;
import com.sap.moc.entity.Department;
import com.sap.moc.entity.Employee;
import com.sap.moc.service.IConsumeService;
import com.sap.moc.service.ICouponService;
import com.sap.moc.service.IDepartmentService;
import com.sap.moc.service.IEmployeeService;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.utils.ConstantReader;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.vo.ConsumeInquiryCriteria;
import com.sap.moc.vo.ConsumeRecordVO;
import com.sap.moc.vo.CouponInquiryCriteria;
import com.sap.moc.vo.CouponInquiryCriteriaBuild;
import com.sap.moc.vo.CouponRecordVO;
import com.sap.moc.vo.KeyAndValueObject;

@Service
public class CouponServiceImpl implements ICouponService {

	@Autowired
	private ICouponRecordDAO couponDAO;

	@Autowired
	private IEmployeeService employeeService;

	@Autowired
	private IConsumeService consumeService;

	@Autowired
	private IDepartmentService departmentService;

	@Override
	public CouponInquiryCriteriaBuild buildInquiryCriteria() {
		
		CouponInquiryCriteriaBuild criteriaBuild = new CouponInquiryCriteriaBuild();

		// Add the coupon registration type

		List<Object> typeList = new ArrayList<Object>();
		KeyAndValueObject kvo = new KeyAndValueObject();
		kvo.setKey(ConstantUtil.COUPON_TYPE_SELF_USE);
		kvo.setValue(ConstantUtil.COUPON_TYPE_SELF_USE_TEXT);
		typeList.add(kvo);

		kvo = new KeyAndValueObject();
		kvo.setKey(ConstantUtil.COUPON_TYPE_FOR_OTHERS);
		kvo.setValue(ConstantUtil.COUPON_TYPE_FOR_OTHERS_TEXT);
		typeList.add(kvo);

		criteriaBuild.setTypeList(typeList);

		// Add the coster centers
		List<Object> departmentList = new ArrayList<Object>();

		List<Department> depList = departmentService.getAllDepartment();
		for (Department dep : depList) {
			KeyAndValueObject kvoDepartment = new KeyAndValueObject();
			kvoDepartment.setKey(dep.getId());
			kvoDepartment.setValue(dep.getName());
			departmentList.add(kvoDepartment);
		}
		criteriaBuild.setCostCenterList(departmentList);

		// Initialized the date
		String dateFormat = ConstantReader.readByKey("DATE_FORMAT");
		if (dateFormat == null) {
			dateFormat = ConstantUtil.DATE_FORMAT_DEFAULT;
		}
		criteriaBuild.setDateFormat(dateFormat);

		Calendar calendar = CommonUtil.getCurrentCalendar();
		// 获取前月的第一天
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		criteriaBuild.setBegda(calendar.getTime());

		calendar = CommonUtil.getCurrentCalendar();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		criteriaBuild.setEndda(calendar.getTime());

		return criteriaBuild;
	}

	@Override
	public List<CouponRecordVO> inquiryByCriteria(CouponInquiryCriteria criteria) throws ParseException {
		
		List<CouponRecordVO> voList = new ArrayList<>();
		String pattern = ConstantReader.readByKey("DATE_FORMAT");
		if (pattern == null) {
			pattern = "yyyy.MM.dd";
		}
		SimpleDateFormat sf = new SimpleDateFormat(pattern);

		List<CouponRecord> cprList = couponDAO.findByInquiryCriteria(criteria);

		for (CouponRecord cpr : cprList) {
			CouponRecordVO vo = new CouponRecordVO();
			vo.setRegDate(cpr.getRegisterDate());
			vo.setRegDateText(sf.format(cpr.getRegisterDate()));
			vo.setEmployeeID(cpr.getEmployee().getId());
			vo.setEmployeeName(cpr.getEmployee().getFirstName() + " " + cpr.getEmployee().getLastName());
			vo.setCostCenterID(cpr.getDepartment().getId());
			vo.setCostCenterName(cpr.getDepartment().getName());
			vo.setCount(String.valueOf(cpr.getCount()));
			vo.setComment(cpr.getComment());
			vo.setType(cpr.getType());

			switch (cpr.getType()) {
			case ConstantUtil.COUPON_TYPE_SELF_USE:
				vo.setTypeText(ConstantUtil.COUPON_TYPE_SELF_USE_TEXT);
				break;
			case ConstantUtil.COUPON_TYPE_FOR_OTHERS:
				vo.setTypeText(ConstantUtil.COUPON_TYPE_FOR_OTHERS_TEXT);
				break;
			default:
				break;
			}

			// for self-use coupon registration, to check whether EE still
			// consumed lunch by scanning QR code
			if (ConstantUtil.COUPON_TYPE_SELF_USE.equals(cpr.getType())) {

				ConsumeInquiryCriteria consumeCritiera = new ConsumeInquiryCriteria();
				consumeCritiera.setBegda(cpr.getRegisterDate());
				consumeCritiera.setEndda(cpr.getRegisterDate());
				consumeCritiera.setEmployeeID(cpr.getEmployee().getId());
				consumeCritiera.setCategoryKey(ConstantUtil.CONSUME_CATEGORY_LUNCH);
				consumeCritiera.setStatusKey(ConstantUtil.CONSUME_STATUS_SUCCESS);

				List<ConsumeRecordVO> consumeList = consumeService.inquiryByCriteria(consumeCritiera);
				if (consumeList.size() > 0) {
					ConsumeRecordVO cr = consumeList.get(0);
					vo.setMisuseType(ConstantUtil.COUPON_MISUESE_TYPE_SCANFORLUNCH);
					vo.setMisuseTypeText(ConstantUtil.COUPON_MISUESE_TYPE_SCANFORLUNCH_TEXT + cr.getVendorName() + " ["
							+ cr.getConsumeTime() + "]");
				}
			}

			voList.add(vo);
		}
		return voList;
	}

	@Override
	public boolean saveCoupon(CouponRecord coupon) {
		return couponDAO.create(coupon);
	}
	
	

	@Override
	public boolean saveCoupon(List<CouponRecord> cpList) {
		return couponDAO.saveOrUpdateAll(cpList);
	}

	@Override
	public boolean saveMultipleCoupon(CouponRecordVO couponVO) {
		Set<String> idSet = couponVO.getEmployeeList();
		List<CouponRecord> saveList = new ArrayList<>();
		for (String id : idSet) {
			CouponRecord cpr = new CouponRecord();
			Employee ee = employeeService.getEmployeeByKey(id);
			cpr.setEmployee(ee);
			cpr.setRegisterDate(couponVO.getRegDate());
			cpr.setComment(couponVO.getComment());
			cpr.setDepartment(ee.getDepartment());
			cpr.setCount(Integer.parseInt(couponVO.getCount()));
			cpr.setType(couponVO.getType());
			
			saveList.add(cpr);
		}
		
		return saveCoupon(saveList);
	}

	@Override
	public Set<String> parseMultipleEmployeeID(CouponRecordVO couponVO) {
		Set<String> idSet = new HashSet<>();

		couponVO.setEmployeeID(couponVO.getEmployeeID().toUpperCase());
		String[] ids;
		ids = couponVO.getEmployeeID().split(",");

		List<String> idList = new ArrayList<String>(Arrays.asList(ids));

		for (int i = 0; i < idList.size(); i++) {
			idSet.add(idList.get(i).trim());
		}
		return idSet;
	}

	@Override
	public String validateCouponBeforeSave(CouponRecordVO couponVO, List<Object> messages) throws ParseException {

		boolean errorOcurred = false;
		boolean warningOccrred = false;

		if (!ConstantUtil.COUPON_TYPE_SELF_USE.equals(couponVO.getType())
				&& !ConstantUtil.COUPON_TYPE_FOR_OTHERS.equals(couponVO.getType())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "Type Check");
			map.put("info", "Invalid type " + couponVO.getType());
			messages.add(map);
			errorOcurred = true;
		}

		if (ConstantUtil.COUPON_TYPE_SELF_USE.equals(couponVO.getType()) && Integer.parseInt(couponVO.getCount()) > 1) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "Count Check");
			map.put("info", "Count number must be 1 for self-use");
			messages.add(map);
			errorOcurred = true;
		}

		if (couponVO.getRegDate() == null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "Date Check");
			map.put("info", "Date is blank");
			messages.add(map);
			errorOcurred = true;
		}

		if (errorOcurred == true) {
			return ConstantUtil.COUPON_VALIDATE_STATUS_FAILED;
		}

		// setup the ConsumeRecord inquiry criteria
		ConsumeInquiryCriteria criteria = new ConsumeInquiryCriteria();
		CouponInquiryCriteria cprCriteria = new CouponInquiryCriteria();
		criteria.setBegda(couponVO.getRegDate());
		criteria.setEndda(couponVO.getRegDate());
		criteria.setCategoryKey(ConstantUtil.CONSUME_CATEGORY_LUNCH);
		criteria.setStatusKey(ConstantUtil.CONSUME_STATUS_SUCCESS);

		// setup the CouponRecord inqiury criteria
		cprCriteria.setBegda(couponVO.getRegDate());
		cprCriteria.setEndda(couponVO.getRegDate());

		cprCriteria.setType(couponVO.getType());

		Set<String> idSet = couponVO.getEmployeeList();
		for (String id : idSet) {

			Employee ee = employeeService.getEmployeeByKey(id);
			if (ee == null) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("info", "Employee doesn't exist");
				messages.add(map);
				errorOcurred = true;
				continue;
			}

			if (ConstantUtil.EMPLOYEE_INACTIVE.equals(ee.getStatus())) {

				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("info", "Employee is in inactive status");
				messages.add(map);
				errorOcurred = true;
				continue;

			}

			// to check whether the EE already had coupon registration today
			cprCriteria.setEmployeeID(id);
			List<CouponRecordVO> cpList = this.inquiryByCriteria(cprCriteria);
			if (cpList.size() > 0) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("info", "Employee already has coupon registration record in the same date");
				messages.add(map);
				warningOccrred = true;
			}

			// to check whether the EE has already consumed Lunch for the
			// registration date or not
			criteria.setEmployeeID(id);
			List<ConsumeRecordVO> crList = consumeService.inquiryByCriteria(criteria);
			if (crList.size() > 0) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				map.put("info", "Employee has already consumed lunch in the same date");
				messages.add(map);
				warningOccrred = true;
			}
		}

		if (errorOcurred) {
			return ConstantUtil.COUPON_VALIDATE_STATUS_FAILED;
		} else if (warningOccrred) {
			return ConstantUtil.COUPON_VALIDATE_STATUS_WARNING;
		} else {
			return ConstantUtil.COUPON_VALIDATE_STATUS_SUCCESS;
		}
	}

	@Override
	public Map<String, String> countTotal(List<CouponRecordVO> list) {

		int selfuseTotal = 0;
		int otheruseTotal = 0;
		int grandTotal = 0;

		for (CouponRecordVO vo : list) {
			switch (vo.getType()) {
			case ConstantUtil.COUPON_TYPE_SELF_USE:

				selfuseTotal = selfuseTotal + Integer.parseInt(vo.getCount());
				break;

			case ConstantUtil.COUPON_TYPE_FOR_OTHERS:
				otheruseTotal = otheruseTotal + Integer.parseInt(vo.getCount());
				break;

			default:
				break;
			}
		}

		grandTotal = selfuseTotal + otheruseTotal;

		Map<String, String> map = new HashMap<>();
		map.put("selfuseTotal", String.valueOf(selfuseTotal));
		map.put("otheruseTotal", String.valueOf(otheruseTotal));
		map.put("grandTotal", String.valueOf(grandTotal));
		return map;
	}

	@Override
	public List<CouponRecordVO> getTodayRecordsByEmployee(String employeeId) {

		CouponInquiryCriteria criteria = new CouponInquiryCriteria();

		criteria.setBegda(CommonUtil.getCurrentTime());
		criteria.setBegda(CommonUtil.getCurrentTime());
		criteria.setType(ConstantUtil.COUPON_TYPE_SELF_USE);
		criteria.setEmployeeID(employeeId);

		List<CouponRecordVO> cpList = new ArrayList<>();
		try {
			cpList = this.inquiryByCriteria(criteria);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return cpList;

	}
}
