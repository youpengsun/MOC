package com.sap.moc.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.moc.dao.IConsumeRecordDAO;
import com.sap.moc.dao.IVendorDAO;
import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.entity.Department;
import com.sap.moc.entity.Employee;
import com.sap.moc.entity.Vendor;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.service.IConsumeService;
import com.sap.moc.service.IDepartmentService;
import com.sap.moc.service.IValidateService;
import com.sap.moc.service.IVendorService;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.utils.ConstantReader;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.utils.TransactionUtil;
import com.sap.moc.vo.ConsumeInquiryCriteria;
import com.sap.moc.vo.ConsumeInquiryCriteriaBuild;
import com.sap.moc.vo.ConsumeRecordVO;
import com.sap.moc.vo.ConsumeSaveResult;
import com.sap.moc.vo.ConsumeValidationResult;
import com.sap.moc.vo.KeyAndValueObject;
import com.sap.moc.vo.QueryTime;
import com.sap.moc.vo.SettlementLine;
import com.sap.moc.vo.SettlementRecord;
import com.sap.moc.vo.SettlementReport;

@Service
public class ConsumeServiceImpl implements IConsumeService {

	@Autowired
	private IVendorDAO vendorDAO;
	@Autowired
	private IConsumeRecordDAO consumeRecordDAO;
	@Autowired
	private IValidateService validateService;
	@Autowired
	private IDepartmentService departmentService;
	@Autowired
	private IVendorService vendorService;

	@Override
	public ConsumeSaveResult saveConsumeRecord(String employeeId, String scanInfo) {

		ConsumeSaveResult saveResult = new ConsumeSaveResult();
		
		//Validate Consume
		ConsumeValidationResult validResult = validateService.checkConsume(employeeId, scanInfo);

		boolean success = validResult.getResult();
		boolean needSave = validResult.isNeedSave();

		Employee employee = validResult.getEmployee();
		VendorLine line = validResult.getVendorLine();

		ConsumeRecord record = new ConsumeRecord(employee, line);
		record.setCategory(validResult.getCategory());

		if (success) {
			record.setStatus(ConstantUtil.CONSUME_STATUS_SUCCESS);
			// Set Transaction Code
			String transactionCode = TransactionUtil.generateCode();
			record.setTransactionCode(transactionCode);

		} else {
			String comment = "";
			ConstantUtil.consumeErrorType errorType = null;
			if (validResult.getError().size() > 0) {
				errorType = validResult.getError().get(0);
				comment = errorType.toString();
			}
			record.setStatus(ConstantUtil.CONSUME_STATUS_FAILED);
			record.setComment(comment);
			
			saveResult.setError(errorType);	
			
			if (validResult.getError().contains(ConstantUtil.consumeErrorType.ALREADY_CONSUMED)) {
				saveResult.setExistRecord(validResult.getExistRecord());
			}
		}

		if (needSave) {
			boolean createSucess = consumeRecordDAO.create(record);
			if (createSucess != true) {
				record.setStatus(ConstantUtil.CONSUME_STATUS_FAILED);
				// Add error message for Created Failed
//				record.getErrors().add(ConstantUtil.consumeErrorType.CREATE_FAILED);
			}
		}
		
		saveResult.setNewRecord(record);		
		return saveResult;
	}

	@Override
	public ConsumeRecord getConsumeRecord(String employeeId) {
		ConsumeRecord record = consumeRecordDAO.findByKey(employeeId);
		return record;
	}

	@Override
	public List<ConsumeRecord> getTodayConsumeRecord(String employeeId, String status, String category) {
		return consumeRecordDAO.getTodayConsumeRecord(employeeId, status, category);
	}

	@Override
	public SettlementReport getSettlementReport(int vendorID, QueryTime time) {

		List<ConsumeRecord> records = consumeRecordDAO.getConsumeRecordsByVendor(vendorID, time);
		Vendor vendor = vendorDAO.findByKey(vendorID);

		SettlementReport report = new SettlementReport(time, vendor, records);

		return report;
	}

	@Override
	public SettlementReport getSettlementReport(int vendorID, Date beginDate, Date endDate) {
		List<ConsumeRecord> records = consumeRecordDAO.getConsumeRecordsByVendor(vendorID, beginDate, endDate);
		Vendor vendor = vendorDAO.findByKey(vendorID);
 
		SettlementReport report = new SettlementReport(beginDate, endDate, vendor, records);
		return report;
	}

	@Override
	public List<ConsumeRecordVO> getRecordsByVendorPeriod(VendorLine line, String status, Timestamp from, Timestamp to) {
		List<ConsumeRecord> crList = consumeRecordDAO.getRecordsByVendorPeriod(line.getId(), status, from, to);
		List<ConsumeRecordVO> voList = new ArrayList<ConsumeRecordVO>();
		//Date format
		String pattern = ConstantReader.readByKey("DATE_FORMAT_OUTPUT");
		if (pattern == null) {
			pattern = "yyyy.MM.dd HH:mm:ss";
		}
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		//Build VO
		for (ConsumeRecord cr : crList) {
			ConsumeRecordVO vo = new ConsumeRecordVO();
			vo.setTransactionCode(cr.getTransactionCode());
			vo.setConsumeTime(sf.format(cr.getTime()));
			vo.setEmployeeID(cr.getEmployee().getId());
			vo.setVendorID(String.valueOf(cr.getVendorLine().getId()));
			voList.add(vo);
		}
		return voList;
	}

	@Override
	public ConsumeInquiryCriteriaBuild buildInquiryCriteria() {

		ConsumeInquiryCriteriaBuild criteriaBuild = new ConsumeInquiryCriteriaBuild();

		// Add the pre-defined consume record status

		List<Object> consumeStatusList = new ArrayList<Object>();
		KeyAndValueObject kvoStatusSuccess = new KeyAndValueObject();
		kvoStatusSuccess.setKey(ConstantUtil.CONSUME_STATUS_SUCCESS);
		kvoStatusSuccess.setValue(ConstantUtil.CONSUME_STATUS_SUCCESS_DESCRIPTION);
		consumeStatusList.add(kvoStatusSuccess);

		KeyAndValueObject kvoStatusFailed = new KeyAndValueObject();
		kvoStatusFailed.setKey(ConstantUtil.CONSUME_STATUS_FAILED);
		kvoStatusFailed.setValue(ConstantUtil.CONSUME_STATUS_FAILED_DESCRIPTION);
		consumeStatusList.add(kvoStatusFailed);

		criteriaBuild.setStatus(consumeStatusList);

		// Add the pre-defined consume record catetory
		List<Object> consumeCategoryList = new ArrayList<Object>();
		KeyAndValueObject kvoCatetoryLunch = new KeyAndValueObject();
		kvoCatetoryLunch.setKey(ConstantUtil.CONSUME_CATEGORY_LUNCH);
		kvoCatetoryLunch.setValue(ConstantUtil.CONSUME_CATEGORY_LUNCH_DESCRIPTION);
		consumeCategoryList.add(kvoCatetoryLunch);

		KeyAndValueObject kvoCatetoryDinner = new KeyAndValueObject();
		kvoCatetoryDinner.setKey(ConstantUtil.CONSUME_CATEGORY_DINNER);
		kvoCatetoryDinner.setValue(ConstantUtil.CONSUME_CATEGORY_DINNER_DESCRIPTION);
		consumeCategoryList.add(kvoCatetoryDinner);

		criteriaBuild.setCategory(consumeCategoryList);

		// Add the coster centers
		List<Object> departmentList = new ArrayList<Object>();

		List<Department> depList = departmentService.getAllDepartment();
		for (Department dep : depList) {
			KeyAndValueObject kvoDepartment = new KeyAndValueObject();
			kvoDepartment.setKey(dep.getId());
			kvoDepartment.setValue(dep.getName());
			departmentList.add(kvoDepartment);
		}
		criteriaBuild.setCostCenter(departmentList);

		// Add the vendors
		//Changed by Darren 2016/9/26
//		List<Object> vendorList = new ArrayList<Object>();

		//List<Vendor> vendors = vendorService.getAllVendor();         
		List<Vendor> vendors = vendorService.getVendorsWithStatus();               

//		for (Vendor vendor : vendors) {
//			KeyAndValueObject kvoVendor = new KeyAndValueObject();
//			kvoVendor.setKey(String.valueOf(vendor.getId()));
//			kvoVendor.setValue(vendor.getName());
//			vendorList.add(kvoVendor);
//		}
		criteriaBuild.setVendor(vendors);

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
	public List<ConsumeRecordVO> inquiryByCriteria(ConsumeInquiryCriteria criteria) throws ParseException {

		List<ConsumeRecordVO> voList = new ArrayList<ConsumeRecordVO>();

		String pattern = ConstantReader.readByKey("DATE_FORMAT_OUTPUT");
		if (pattern == null) {
			pattern = "yyyy.MM.dd HH:mm:ss";
		}
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		List<ConsumeRecord> crList = consumeRecordDAO.findByInquiryCriteria(criteria);

		for (ConsumeRecord cr : crList) {
			ConsumeRecordVO vo = new ConsumeRecordVO();

			vo.setTransactionCode(cr.getTransactionCode());
			vo.setConsumeTime(sf.format(cr.getTime()));
			vo.setEmployeeID(cr.getEmployee().getId());
			vo.setEmployeeName(cr.getEmployee().getFirstName() + " " + cr.getEmployee().getLastName());
			vo.setCostCenterID(cr.getEmployee().getDepartment().getId());
			vo.setCostCenterName(cr.getEmployee().getDepartment().getName());
			vo.setVendorID(String.valueOf(cr.getVendorLine().getVendor().getId()));
			vo.setVendorName(cr.getVendorLine().getVendor().getName());
			vo.setVendorLineNumber(String.valueOf(cr.getVendorLine().getLineNO()));
			vo.setVendorLineName(cr.getVendorLine().getName());

			if (cr.getStatus() != null) {
				switch (cr.getStatus()) {
				case ConstantUtil.CONSUME_STATUS_SUCCESS:
					vo.setStatus(ConstantUtil.CONSUME_STATUS_SUCCESS_DESCRIPTION);
					break;
				case ConstantUtil.CONSUME_STATUS_FAILED:
					vo.setStatus(ConstantUtil.CONSUME_STATUS_FAILED_DESCRIPTION);
					break;
				default:
					break;
				}
			}

			if (cr.getCategory() != null) {
				switch (cr.getCategory()) {
				case ConstantUtil.CONSUME_CATEGORY_DINNER:
					vo.setCategory(ConstantUtil.CONSUME_CATEGORY_DINNER_DESCRIPTION);
					break;
				case ConstantUtil.CONSUME_CATEGORY_LUNCH:
					vo.setCategory(ConstantUtil.CONSUME_CATEGORY_LUNCH_DESCRIPTION);
				default:
					break;
				}
			}

			voList.add(vo);
		}
		return voList;
	}

	@Override
	public int getVendorLineTodayActiveCount(int vendorLineID) {

		Calendar calBegda = CommonUtil.getCurrentCalendar();
		calBegda.set(Calendar.HOUR_OF_DAY, 0);
		calBegda.set(Calendar.MINUTE, 0);
		calBegda.set(Calendar.SECOND, 0);
		java.util.Date begda = calBegda.getTime();

		Calendar calEndda = CommonUtil.getCurrentCalendar();
		calEndda.set(Calendar.HOUR_OF_DAY, 23);
		calEndda.set(Calendar.MINUTE, 59);
		calEndda.set(Calendar.SECOND, 59);
		java.util.Date endda = calEndda.getTime();

		int count = consumeRecordDAO.getVendorLineCountByDate(vendorLineID, begda, endda,
				ConstantUtil.CONSUME_STATUS_SUCCESS);
		return count;
	}

	@Override
	public byte[] buildExcelOfMonthlySettlement(SettlementReport settlement) {
		final String[] title = { "序号","业务码","消费时间","工号" };

		XSSFWorkbook wb = new XSSFWorkbook();
		// add sheet
		XSSFSheet sheet = wb.createSheet("sheet");
		sheet.setDisplayGridlines(false);
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeightInPoints(15f);
		// font
		XSSFFont headerFont = wb.createFont();
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		
		XSSFFont tableHeaderFont = wb.createFont();
		tableHeaderFont.setFontHeightInPoints((short) 10);
		tableHeaderFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		
		XSSFFont tableBodyFont = wb.createFont();
		tableBodyFont.setFontHeightInPoints((short) 10);
		tableBodyFont.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		
		// cell style		
		XSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);		
		headerStyle.setFont(headerFont);
		headerStyle.setWrapText(false);
		headerStyle.setBorderBottom(XSSFCellStyle.BORDER_THICK);
		
		
		XSSFCellStyle header2Style = wb.createCellStyle();
		header2Style.setAlignment(XSSFCellStyle.ALIGN_LEFT);		
		header2Style.setFont(tableHeaderFont);
		header2Style.setWrapText(false);
		
		XSSFCellStyle infoStyle = wb.createCellStyle();
		infoStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);		
		infoStyle.setFont(tableBodyFont);
		infoStyle.setWrapText(false);
		infoStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		
		XSSFCellStyle tableHeaderStyle = wb.createCellStyle();
		tableHeaderStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 指定单元格居中对齐
		tableHeaderStyle.setFont(tableHeaderFont);
		tableHeaderStyle.setWrapText(false);
		tableHeaderStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		tableHeaderStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		tableHeaderStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		tableHeaderStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		XSSFCellStyle tableBodyStyle = wb.createCellStyle();
		tableBodyStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);		
		tableBodyStyle.setFont(tableBodyFont);
		tableBodyStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		tableBodyStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		tableBodyStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		// // 设置列宽
		// sheet.setColumnWidth((short)0, (short)9600);
		// sheet.setColumnWidth((short)1, (short)4000);
		// sheet.setColumnWidth((short)2, (short)8000);
		// sheet.setColumnWidth((short)3, (short)8000);

		// write total
		Row row = sheet.createRow(1);
		Cell cell = row.createCell(0);
		cell.setCellValue(settlement.getVendorName() + "对账单");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(1);
		cell.setCellStyle(headerStyle);
		cell = row.createCell(2);
		cell.setCellStyle(headerStyle);
		cell = row.createCell(3);
		cell.setCellStyle(headerStyle);
		
		row = sheet.createRow(3);
		cell = row.createCell(0);
		cell.setCellStyle(header2Style);
		cell.setCellValue("对账时段");
		cell = row.createCell(1);
		cell.setCellStyle(infoStyle);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		cell.setCellValue(sdf.format(settlement.getBeginDate()) + '-'
				+ sdf.format(settlement.getEndDate()));
		cell = row.createCell(2);
		cell.setCellStyle(infoStyle);
		
		row = sheet.createRow(5);
		cell = row.createCell(0);
		cell.setCellStyle(header2Style);
		cell.setCellValue("消费总数");
		cell = row.createCell(1);
		cell.setCellStyle(infoStyle);
		cell.setCellValue(settlement.getTotalCount());
		cell = row.createCell(2);
		cell.setCellStyle(infoStyle);
		
		// write table header
		row = sheet.createRow(7);
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(tableHeaderStyle);

		}

		// write row
		Integer rowNum = 8;
		Integer count = 1;
		// List<SettlementLine> lineRecords = settlement.getLineRecords();
		for (SettlementLine lineRecord : settlement.getLineRecords()) {
			for (SettlementRecord record : lineRecord.getDetailRecords()) {
				row = sheet.createRow(rowNum);
				/* Column 1: no */
				cell = row.createCell(0);
				cell.setCellValue(count);
				cell.setCellStyle(tableBodyStyle);
				/* Column 2: transaction code */
				cell = row.createCell(1);
				cell.setCellValue(record.getTransactionCode());
				cell.setCellStyle(tableBodyStyle);
				/* Column 3: consume time */
				cell = row.createCell(2);
				cell.setCellValue(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(record.getTime().getTime()));
				cell.setCellStyle(tableBodyStyle);
				/* Column 4: employee id */
				cell = row.createCell(3);
				String employeeId = record.getEmployeeId();
				if ( employeeId.length() >= 7 ) {
					employeeId = employeeId.replaceFirst(employeeId.substring(2, 5), "***");
				}				
				cell.setCellValue(employeeId);
				cell.setCellStyle(tableBodyStyle);
				rowNum++; count++;
			}
		}
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			wb.write(output);
			output.flush();
			output.close();
			wb.close();
			System.out.println("Monthly Settlement Excel Generated!");
			return output.toByteArray();
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}
}
