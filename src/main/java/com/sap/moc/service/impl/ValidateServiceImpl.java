/**
 * 
 */
package com.sap.moc.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.moc.dao.IConsumeCalendarDAO;
import com.sap.moc.dao.IContractDAO;
import com.sap.moc.dao.IVendorLineDAO;
import com.sap.moc.entity.ConsumeCalendar;
import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.entity.Contract;
import com.sap.moc.entity.Employee;
import com.sap.moc.entity.Vendor;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.service.IConsumeService;
import com.sap.moc.service.ICouponService;
import com.sap.moc.service.IEmployeeService;
import com.sap.moc.service.IValidateService;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.utils.ConstantReader;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.utils.TimeCheckUtil;
import com.sap.moc.vo.ConsumeValidationResult;
import com.sap.moc.vo.CouponRecordVO;
import com.sap.moc.vo.EmployeeUpload;
import com.sap.moc.vo.QRCode;

/**
 * @author I074184
 *
 */
@Service
public class ValidateServiceImpl implements IValidateService {

	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private IVendorLineDAO vendorLineDAO;
	@Autowired
	private IConsumeService consumeService;
	@Autowired
	private IContractDAO contractDAO;
	@Autowired
	private IConsumeCalendarDAO calendarDAO;
	@Autowired
	private ICouponService couponService;

	/**
	 * check whether consume can be successful
	 * 
	 * @param employeeId
	 * @param scanInfo
	 * @return
	 */
	@Override
	public ConsumeValidationResult checkConsume(String employeeId, String scanInfo) {
		ConsumeValidationResult result = new ConsumeValidationResult();
		List<ConstantUtil.consumeErrorType> errors = new ArrayList<ConstantUtil.consumeErrorType>();

		// TODO for testing
		// String testUsers = ConstantReader.readByKey("TEST_USERS");
		// String[] users = testUsers.split("&");
		// if ( Arrays.asList(users).contains(employeeId) ) {
		// result.setResult(true);
		// result.setCategory(ConstantUtil.CONSUME_CATEGORY_LUNCH);
		// return result;
		// }
		//

		result.setNeedSave(false);
		QRCode qrCode = new QRCode(scanInfo);

		// Check Employee
		validateResult<Employee> validateEE = validateEmployee(employeeId);
		if (validateEE.getEntry() != null) {
			result.setEmployee(validateEE.getEntry());
		}

		if (!qrCode.isValid()) {
			errors.add(ConstantUtil.consumeErrorType.INVALID_QRCODE);
		} else {

			int vendorId = qrCode.getVendorId();

			// Get Current Meal Category(Lunch/Dinner)
			String consumeCategory = TimeCheckUtil.getCurrentCategory();
			result.setCategory(consumeCategory);

			if (validateEE.getEntry() == null) {
				errors.add(ConstantUtil.consumeErrorType.INVALID_EE);
			} else {
				if (!validateEE.isValid()) {
					errors.add(ConstantUtil.consumeErrorType.INACTIVE_EE);
				}
			}

			// Check VendorLine

			validateResult<VendorLine> validateVendorLine = validateVendorLine(qrCode);

			if (!errors.contains(ConstantUtil.consumeErrorType.INVALID_EE)) {
				if (validateVendorLine.getEntry() == null) {
					errors.add(ConstantUtil.consumeErrorType.INVALID_VENDORLINE);
				} else {
					result.setVendorLine(validateVendorLine.getEntry());
				}
			}

			if (errors.contains(ConstantUtil.consumeErrorType.INVALID_EE)
					|| errors.contains(ConstantUtil.consumeErrorType.INVALID_VENDORLINE)) {
				result.setNeedSave(false);
			} else {
				result.setNeedSave(true);
			}

			String testMode = ConstantReader.readSysParaByKey("TEST_MODE");

			if (errors.size() == 0 && !testMode.equals("1")) {
				// Check Vendor
				if (!validateVendor(vendorId)) {
					errors.add(ConstantUtil.consumeErrorType.INVALID_VENDOR);
				}
				// Check Consume Date by workday and consume Calendar
				else if (!validateConsumeDate()) {
					errors.add(ConstantUtil.consumeErrorType.INVALID_DATE);
				}
				// Check Consume Time (Lunch, Dinner, ...)
				else if (!validateConsumeTime(employeeId, consumeCategory, result.getVendorLine().getVendor())) {
					errors.add(ConstantUtil.consumeErrorType.NOT_AVAILABLE_TIME);
				}
				// Check Coupon Consumed
				else if (!validateCouponConsumed(employeeId)) {
					errors.add(ConstantUtil.consumeErrorType.COUPON_CONSUMED);
				}
				else {
					// Already Consumed
					validateResult<ConsumeRecord> validateAlreadyConsumed = validateAlreadyConsumed(employeeId, consumeCategory);
					if (!validateAlreadyConsumed.valid) {
						errors.add(ConstantUtil.consumeErrorType.ALREADY_CONSUMED);
						result.setExistRecord(validateAlreadyConsumed.entry);
					}
				}
			}
		}
		if (errors.size() > 0) {
			result.setResult(false);
			result.setError(errors);
		} else {
			result.setResult(true);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sap.moc.service.IValidateService#validateEmployeeData(com.sap.moc.
	 * entity.Employee, java.lang.String)
	 */
	@Override
	public String validateEmployeeData(Employee ee, String act,String mode) {
		StringBuilder sb = new StringBuilder();
		String ID = ee.getId();
		if (ID == null || ID.equals("")) {
			sb.append("ID Number is mandatory;");
		} else if (ID.charAt(0) != 'I' && ID.charAt(0) != 'D' && ID.charAt(0) != 'C') {
			sb.append("ID start character is incorrect");
		} else if (((ID.startsWith("D") || ID.startsWith("I")) && ID.length() != 7)
				|| (ID.startsWith("C") && ID.length() != 8)) {
			sb.append("ID length is incorrect;");
		} else {
			if (EmployeeUpload.insert.equals(act)) {
				Employee ee_insert = employeeService.getEmployeeByKey(ID);
				if (ee_insert != null) {
					sb.append("Employee already exists;");
				}
			}
		}
		if (ee.getLastName().isEmpty()) {
			sb.append("LastName is mandatory;");
		}
		if (ee.getFirstName().isEmpty()) {
			sb.append("FirstName is mandatory;");
		}
		String telNo = ee.getTelNo();
		if (telNo == null || telNo.isEmpty()) {
			sb.append("Cellphone is mandatory;");
		} else {
			if (!telNo.matches("[0-9]*")) {
				sb.append("Cellphone contails invalid character;");
			}
		}

		if (ee.getDepartment() == null) {
			sb.append("Cost Center is mandatory;");
		} else if (EmployeeUpload.MODE_BATCH.equals(mode)) {  //for Excel
			if (ee.getDepartment().getName() == null || ee.getDepartment().getName().isEmpty()) {
				sb.append("Cost Center Description is mandatory;");
			}
		}

		if (ee.getEmail() != null && !ee.getEmail().isEmpty()) {
			if (!ee.getEmail().matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@sap.com$")) {
				sb.append("Email not valid;");
			}
		}

		return sb.toString();
	}

	class validateResult<T> {
		private T entry;

		private boolean valid;

		public T getEntry() {
			return entry;
		}

		public void setEntry(T entry) {
			this.entry = entry;
		}

		public boolean isValid() {
			return valid;
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}

		public validateResult() {
			this.valid = false;
		}

	}

	/**
	 * check employee is active
	 * 
	 * @param employeeId
	 * @return isActive
	 */
	private validateResult<Employee> validateEmployee(String employeeId) {
		validateResult<Employee> result = new validateResult<>();
		Employee ee = employeeService.getEmployeeByKey(employeeId);

		if (ee != null) {
			result.entry = ee;

			String status = ee.getStatus();
			if (ConstantUtil.EMPLOYEE_ACTIVE.equals(status)) {
				result.valid = true;
			}
		}
		return result;
	}

	private validateResult<VendorLine> validateVendorLine(QRCode qrCode) {
		validateResult<VendorLine> result = new validateResult<>();
		VendorLine line = vendorLineDAO.getLinebyQRCode(qrCode);
		if (line != null) {
			result.entry = line;
			result.valid = true;
		}
		return result;
	}

	private boolean validateVendor(int vendorId) {

		boolean result = false;
		// Contract Valid
		Date today = new Date(CommonUtil.getCurrentTime().getTime());
		List<Contract> contracts = contractDAO.getCurrentContracts(vendorId, today);
		if (contracts.size() > 0) {
			result = true;
		}
		return result;
	}

	// Valid Date
	private boolean validateConsumeDate() {
		boolean result = false;

		// 1.Check ConsumeCalendar
		ConsumeCalendar consumeCalendar = calendarDAO.getTodayCalendar();
		if (consumeCalendar != null) {
			result = consumeCalendar.isMealValid();
		} else {
			// 2.Check Workday
			Calendar calendar = CommonUtil.getCurrentCalendar();
			calendar.setTime(CommonUtil.getCurrentTime());
			int dayNo = calendar.get(Calendar.DAY_OF_WEEK);

			boolean workDayFalg = false;

			if (dayNo != Calendar.SATURDAY && dayNo != Calendar.SUNDAY) {
				workDayFalg = true;
			}
			result = workDayFalg;
		}

		return result;
	}

	// Valid Time
	private boolean validateConsumeTime(String employeeId, String consumeCategory, Vendor vendor) {
		boolean result = false;

		if (consumeCategory.equals(ConstantUtil.CONSUME_CATEGORY_LUNCH)) {
			result = true;
		} else if (consumeCategory.equals(ConstantUtil.CONSUME_CATEGORY_DINNER)) {
			if (vendor.getVendorType().equals(ConstantUtil.VENDOR_TYPE_INTERNAL)) {
				result = true;
			}
		}
		return result;
	}

	// Already Consumed
	private validateResult<ConsumeRecord> validateAlreadyConsumed(String employeeId, String consumeCategory) {

		validateResult<ConsumeRecord> result = new validateResult<>();
		List<ConsumeRecord> records = consumeService.getTodayConsumeRecord(employeeId,
				ConstantUtil.CONSUME_STATUS_SUCCESS, consumeCategory);

		if (records.size() == 0) {
			result.valid = true;
		} else {
			result.valid = false;
			result.entry = records.get(0);
		}
		return result;
	}

	// Check Coupon
	private boolean validateCouponConsumed(String employeeId) {
		boolean result = true;

		// Check Coupon Consume
		List<CouponRecordVO> list = couponService.getTodayRecordsByEmployee(employeeId);
		if (list.size() > 0) {
			result = false;
		}

		return result;
	}

}
