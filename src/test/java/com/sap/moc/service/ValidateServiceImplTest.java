package com.sap.moc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.sap.moc.dao.IConsumeCalendarDAO;
import com.sap.moc.entity.ConsumeCalendar;
import com.sap.moc.test.BaseJunit4Test;
import com.sap.moc.utils.ConstantUtil.consumeErrorType;
import com.sap.moc.vo.ConsumeValidationResult;

public class ValidateServiceImplTest extends BaseJunit4Test {

	@Autowired
	IValidateService ValidateService;

	@Autowired
	IConsumeCalendarDAO calendarDao;

	@Test
	@Rollback(false)
	public void testCheckEmployeeActive() {
		System.out.println("begin");
		// System.out.println(ValidateService.checkEmployeeActive("I074193"));
	}

	@Test
	public void testCheckVendorActive() {
		// fail("Not yet implemented");
	}

	@Test
	public void testCheckCalendar() {
		ConsumeCalendar calendar = calendarDao.getTodayCalendar();
		assertNull(calendar);
	}

//	@Test
	@Rollback(false)
	public void testSaveCalendar() {
		ConsumeCalendar calendar = new ConsumeCalendar();
		calendar.setDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));

		calendar.setHolidayName("testHoliday");
		calendar.setMealValid(true);

		boolean result = calendarDao.saveOrUpdate(calendar);
		assertTrue(result);
	}

	@Test
	public void testCheckConsume_WrongScanCode_1() {
		// Wrong ScanCode
		String employeeId = "11";
		String scanCode = "wrongcode";
		ConsumeValidationResult result = ValidateService.checkConsume(employeeId, scanCode);

		assertEquals(1, result.getError().size());
		System.out.println(result.getError().get(0));
		assertEquals(consumeErrorType.INVALID_QRCODE, result.getError().get(0));
		// assertEquals("", result.getCategory());
		assertEquals(false, result.isNeedSave());
		assertEquals(false, result.getResult());
		assertNull(result.getEmployee());
		assertNull(result.getVendorLine());
	}

	@Test
	public void testCheckConsume_WrongScanCode_2() {
		// Wrong ScanCode
		String employeeId = "11";
		String scanCode = "1111111";
		ConsumeValidationResult result = ValidateService.checkConsume(employeeId, scanCode);

		assertEquals(1, result.getError().size());
		System.out.println(result.getError().get(0));
		assertEquals(consumeErrorType.INVALID_QRCODE, result.getError().get(0));
		// assertEquals("", result.getCategory());
		assertEquals(false, result.isNeedSave());
		assertEquals(false, result.getResult());
		assertNull(result.getEmployee());
		assertNull(result.getVendorLine());
	}

	@Test
	public void testCheckConsume_WrongScanCode_3() {
		// Wrong ScanCode
		String employeeId = "11";
		String scanCode = "xxxx-22";
		ConsumeValidationResult result = ValidateService.checkConsume(employeeId, scanCode);

		assertEquals(1, result.getError().size());
		System.out.println(result.getError().get(0));
		assertEquals(consumeErrorType.INVALID_QRCODE, result.getError().get(0));
		// assertEquals("", result.getCategory());
		assertEquals(false, result.isNeedSave());
		assertEquals(false, result.getResult());
		assertNull(result.getEmployee());
		assertNull(result.getVendorLine());
	}

	@Test
	public void testCheckConsume_InvalidEE() {
		// Wrong ScanCode
		String employeeId = "11";
		String scanCode = "10010-1";
		ConsumeValidationResult result = ValidateService.checkConsume(employeeId, scanCode);

		assertEquals(1, result.getError().size());
		System.out.println(result.getError().get(0));
		assertEquals(consumeErrorType.INVALID_EE, result.getError().get(0));
		// assertEquals("", result.getCategory());
		assertEquals(false, result.isNeedSave());
		assertEquals(false, result.getResult());
		assertNull(result.getEmployee());
		assertNull(result.getVendorLine());
	}

	@Test
	public void testCheckConsume_InActiveEE() {
		// Wrong ScanCode
		String employeeId = "I074104";
		String scanCode = "10010-1";
		ConsumeValidationResult result = ValidateService.checkConsume(employeeId, scanCode);

		assertEquals(1, result.getError().size());
		System.out.println(result.getError().get(0));
		assertEquals(consumeErrorType.INACTIVE_EE, result.getError().get(0));
		// assertEquals("", result.getCategory());
		assertEquals(true, result.isNeedSave());
		assertEquals(false, result.getResult());
		assertNotNull(result.getEmployee());
		assertNotNull(result.getVendorLine());
	}

	@Test
	public void testCheckConsume_Valide_EE() {
		// Wrong ScanCode
		String employeeId = "I074101";
		String scanCode = "10010-1";
		ConsumeValidationResult result = ValidateService.checkConsume(employeeId, scanCode);

		assertEquals(0, result.getError().size());
		// assertEquals(consumeErrorType.INACTIVE_EE, result.getError().get(0));
		// assertEquals("", result.getCategory());
		assertEquals(true, result.isNeedSave());
		assertEquals(true, result.getResult());
		assertNotNull(result.getEmployee());
		assertNotNull(result.getVendorLine());
	}

	@Test
	public void testCheckConsume_Invalid_line() {
		// Wrong ScanCode
		String employeeId = "I074101";
		String scanCode = "10010-100";
		ConsumeValidationResult result = ValidateService.checkConsume(employeeId, scanCode);

		assertEquals(1, result.getError().size());
		System.out.println(result.getError().get(0));
		assertEquals(consumeErrorType.INVALID_VENDORLINE, result.getError().get(0));
		// assertEquals("", result.getCategory());
		assertEquals(false, result.isNeedSave());
		assertEquals(false, result.getResult());
		assertNotNull(result.getEmployee());
		assertNull(result.getVendorLine());
	}
	
	@Test
	public void TestCheckAlready_Consumed(){
		String employeeId = "I065892";
		String scanCode = "10010-100";
		ConsumeValidationResult result = ValidateService.checkConsume(employeeId, scanCode);
		
		assertEquals(1, result.getError().size());
		System.out.println(result.getError().get(0));
		assertEquals(consumeErrorType.INVALID_VENDORLINE, result.getError().get(0));
		// assertEquals("", result.getCategory());
		assertEquals(false, result.isNeedSave());
		assertEquals(false, result.getResult());
		assertNotNull(result.getEmployee());
		assertNull(result.getVendorLine());
	}

}
