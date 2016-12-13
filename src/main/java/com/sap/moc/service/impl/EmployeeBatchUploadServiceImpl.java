package com.sap.moc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sap.moc.entity.Department;
import com.sap.moc.entity.Employee;
import com.sap.moc.service.IDepartmentService;
import com.sap.moc.service.IEmployeeBatchUploadService;
import com.sap.moc.service.IEmployeeService;
import com.sap.moc.service.IValidateService;
import com.sap.moc.utils.ConstantReader;
import com.sap.moc.utils.TrimUtil;
import com.sap.moc.vo.EmployeeUpload;
import com.sap.moc.vo.Language;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * @author I074115
 *
 */
@Service
public class EmployeeBatchUploadServiceImpl implements IEmployeeBatchUploadService {
	@Autowired
	IEmployeeService employeeService;
	@Autowired
	IDepartmentService departmentService;
	@Autowired
	IValidateService validateEE;

	@Override
	public List<EmployeeUpload> handleExcelUpload(MultipartFile eeexcel, StringBuilder msg) {
		List<EmployeeUpload> uploadEE = new ArrayList<EmployeeUpload>();
		// get InputStream of Excel
		InputStream is = null;
		try {
			is = eeexcel.getInputStream();
			if (is != null) {
				uploadEE = parseExcelFile(is, msg);
				is.close();
			}
		} catch (IOException e) {
			msg.append(e.getMessage());
		}
		return uploadEE;
	}

	// Upload Excel File
	// private InputStream uploadExcelFile(HttpServletRequest request,
	// StringBuilder msg) {
	// InputStream stream = null;
	// FileItemIterator iter = null;
	// FileItemStream item = null;
	// // Parse the request
	// try {
	// ServletFileUpload upload = new ServletFileUpload();
	// iter = upload.getItemIterator(request);
	// while (iter.hasNext()) {
	// item = iter.next();
	// if (!item.isFormField()) { // Only consider file
	// stream = item.openStream();
	// break; // Only one Excel file allowed
	// }
	// }
	// } catch (FileUploadException e) {
	// System.out.println(e.getMessage());
	// e.printStackTrace();
	// msg.append(e.getMessage());
	// } catch (IOException e) {
	// System.out.println(e.getMessage());
	// e.printStackTrace();
	// msg.append(e.getMessage());
	// }
	// return stream;
	// }

	// Parse Excel File
	private List<EmployeeUpload> parseExcelFile(InputStream stream, StringBuilder msg) {
		List<EmployeeUpload> uploadEmployees = new ArrayList<EmployeeUpload>();
		Map<String, Department> newDepartment = new HashMap<String, Department>();
		Map<String, String> uniqueEE = new HashMap<String, String>();
		EmployeeUpload ee_upload = null;
		String key = null;
		boolean duplicate = false;
		// Get all existing employees from database
		Map<String, Employee> allDBEmployees = getAllDBEmployees();
		// Get all existing departments from database
		Map<String, Department> allDBDepartments = getAllDBDepartments();
		// Access Excel
		try {
			Workbook workbook = Workbook.getWorkbook(stream);
			Sheet sheet = workbook.getSheet(0);
			int rowCount = sheet.getRows();
			boolean ifUploadExcel = verifyExcelContent(sheet, rowCount);
			if (ifUploadExcel == true) {
				for (int row = 1; row < rowCount; row++) { // exclude title
					key = sheet.getCell(0, row).getContents().toUpperCase().trim();
					// Ignore initial line
					if (key.isEmpty()) {
						continue;
					}
					// return if duplicate keys exist
					String eeKey = uniqueEE.get(key);
					if (eeKey == null) { // Unique
						uniqueEE.put(key, key);
					} else {
						duplicate = true; // duplicate
						break;
					}
					// Only when key exists
					ee_upload = parseExcelRow(sheet.getRow(row), allDBEmployees.get(key), newDepartment,
							allDBDepartments);
					if (ee_upload != null) {
						ee_upload.setExcelRow(row + 1);
						uploadEmployees.add(ee_upload);
					}
				}
				if (duplicate == false) {
					if (uploadEmployees.isEmpty()) {
						msg.append("No data needs update");
					} else {
						boolean isOK = filterErrorEntry(uploadEmployees);
						// Add new Departments when no error
						if (isOK == true && !newDepartment.isEmpty()) {
							createNewDepartments(newDepartment);
						}
					}
				} else {
					msg.append("Duplicate employee IDs exist,remove the duplicate Excel row");
					uploadEmployees.clear();
				}
			} else {
				msg.append("Excel Contents Invalid");
			}
			// close workbook
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
			msg.append(e.getMessage());
		}
		return uploadEmployees;
	}

	// Verify if the Excel is for EE Upload
	private boolean verifyExcelContent(Sheet sheet, int rowCount) {
		boolean res = true;
		// Compare the mandatory column name
		if (rowCount >= 1) {
			if (sheet.getCell(0, 0).getContents().trim().equals(ConstantReader.readByKey("COLUMN0"))
					&& sheet.getCell(1, 0).getContents().trim().equals(ConstantReader.readByKey("COLUMN1"))
					&& sheet.getCell(2, 0).getContents().trim().equals(ConstantReader.readByKey("COLUMN2"))
					&& sheet.getCell(3, 0).getContents().trim().equals(ConstantReader.readByKey("COLUMN3"))
					&& sheet.getCell(8, 0).getContents().trim().equals(ConstantReader.readByKey("COLUMN8"))) {
				res = true;
			} else {
				res = false;
			}
		} else {
			res = false;
		}
		return res;
	}

	// Retrieve all DB employees and construct Map
	private Map<String, Employee> getAllDBEmployees() {
		Map<String, Employee> map = new HashMap<String, Employee>();
		// Get Employees
		List<Employee> list = employeeService.getAllEmployee();
		Employee ee = null;
		String key = null;
		// Build a map for query
		Iterator<Employee> iterator = list.iterator();
		while (iterator.hasNext()) {
			ee = iterator.next();
			key = ee.getId();
			map.put(key, ee);
		}
		return map;
	}
	
	// Retrieve all DB departments and construct Map
	private Map<String, Department> getAllDBDepartments() {
		Map<String, Department> map = new HashMap<String, Department>();
		// Get Departments
		List<Department> list = departmentService.getAllDepartment();
		Department depart = null;
		String key = null;
		// Build a map for query
		Iterator<Department> iterator = list.iterator();
		while (iterator.hasNext()) {
			depart = iterator.next();
			key = depart.getId();
			map.put(key, depart);
		}
		return map;
	}

	private EmployeeUpload parseExcelRow(Cell[] row, Employee dbEmployee, Map<String, Department> newDepartment,Map<String,Department> allDBDepartments) {
		EmployeeUpload ee_upload = null;

		String employeeID = ""; // Excel Column 1
		String lastName = ""; // Excel Column 2
		String firstName = ""; // Excel Column 3
		String cellphone = ""; // Excel Column 4
		String email = ""; // Excel Column 5
		String wechatID = ""; // Excel Column 6
		String area = ""; // Excel Column 7
		String subArea = ""; // Excel Column 8
		String costcenter = ""; // Excel Column 9
		String costcentertext = ""; // Excel Column 10
		String language = ""; // Excel Column 11
		Language lang = null;

		// parse row
		for (int col = 0; col < row.length; col++) {
			// avoid ArrayIndexOutOfBound Exception
			switch (col) {
			case 0:
				employeeID = row[col].getContents().toUpperCase().trim();
				break;
			case 1:
				lastName = row[col].getContents().trim();
				break;
			case 2:
				firstName = row[col].getContents().trim();
				break;
			case 3:
				cellphone = row[col].getContents().trim();
				break;
			case 4:
				email = row[col].getContents().trim();
				break;
			case 5:
				wechatID = row[col].getContents().trim();
				break;
			case 6:
				area = row[col].getContents().trim();
				break;
			case 7:
				subArea = row[col].getContents().trim();
				break;
			case 8:
				costcenter = row[col].getContents().trim();
				break;
			case 9:
				costcentertext = row[col].getContents().trim();
				break;
			case 10:
				language = row[col].getContents().trim().toUpperCase();
				break;
			}
		}
		switch (language) {
		case "EN":
			lang = Language.ENGLISH;
			break;
		case "ZH":
			lang = Language.CHINESE;
			break;
		default:
			lang = Language.ENGLISH;
			break;
		}

		// Build Employee entity
		Employee tempEE = new Employee();
		tempEE.setId(employeeID);
		tempEE.setLastName(lastName);
		tempEE.setFirstName(firstName);
		tempEE.setTelNo(cellphone);
		tempEE.setEmail(email);
		tempEE.setWechatId(wechatID);
		tempEE.setPersonalArea(area);
		tempEE.setPersonalSubarea(subArea);
		tempEE.setLanguage(lang);

		String departmentId = String.valueOf(TrimUtil.trimNumber(costcenter));
		String departName = costcentertext;
		// Keep 32 length
		if (costcentertext.length() > 32) {
			departName = costcentertext.substring(0, 31);
		}
		if (!departmentId.equals("0")) {
			Department department = allDBDepartments.get(departmentId);
			if (department != null) { // department exists
				tempEE.setDepartment(department);
			} else { // department not exists
				department = new Department(departmentId, departName);
				tempEE.setDepartment(department);
				// New Department
				newDepartment.put(costcenter, department);
			}
		}
		// Data Validation
		String act = null;
		if (dbEmployee == null) {
			act = EmployeeUpload.insert;
		} else {
			act = EmployeeUpload.update;
		}
		String errorText = validateEE.validateEmployeeData(tempEE, act, EmployeeUpload.MODE_BATCH);

		// added to list
		if (!errorText.isEmpty()) {
			ee_upload = new EmployeeUpload();
			ee_upload.setEmployee(tempEE);
			ee_upload.setStatus(EmployeeUpload.error);
			ee_upload.setMessage(errorText);
		} else if (dbEmployee == null) { // not existing in DB
			ee_upload = new EmployeeUpload();
			ee_upload.setEmployee(tempEE);
			ee_upload.setStatus(EmployeeUpload.insert);
			ee_upload.setMessage("To be Inserted");
		} else if (dbEmployee != null) {
			// Compare one by one
			if (!tempEE.getLastName().equals(dbEmployee.getLastName())
					|| !tempEE.getFirstName().equals(dbEmployee.getFirstName())
					|| !tempEE.getEmail().equals(dbEmployee.getEmail())
					|| !tempEE.getTelNo().equals(dbEmployee.getTelNo())
					|| !tempEE.getWechatId().equals(dbEmployee.getWechatId())
					|| !tempEE.getPersonalArea().equals(dbEmployee.getPersonalArea())
					|| !tempEE.getPersonalSubarea().equals(dbEmployee.getPersonalSubarea())
					|| !tempEE.getLanguage().equals(dbEmployee.getLanguage()) || dbEmployee.getDepartment() == null
					|| (dbEmployee.getDepartment() != null
							&& !tempEE.getDepartment().getId().equals(dbEmployee.getDepartment().getId()))) {
				ee_upload = new EmployeeUpload();
				ee_upload.setEmployee(tempEE);
				ee_upload.setStatus(EmployeeUpload.update);
				ee_upload.setMessage("To be Updated");
			}

		}

		return ee_upload;

	}

	// Return error EE if exists
	private boolean filterErrorEntry(List<EmployeeUpload> allEE) {
		boolean isOK = true;
		List<EmployeeUpload> errorEE = new ArrayList<EmployeeUpload>();
		for (EmployeeUpload ee : allEE) {
			if (ee.getStatus().equals(EmployeeUpload.error)) {
				errorEE.add(ee);
			}
		}
		// If error happens, only show Error EEs
		if (!errorEE.isEmpty()) {
			allEE.clear();
			allEE.addAll(errorEE);
			isOK = false;
		}
		return isOK;
	}

	// New Departments
	private void createNewDepartments(Map<String, Department> department) {
		List<Department> list = new ArrayList<Department>();
		for (String key : department.keySet()) {
			list.add(department.get(key));
		}
		// Update to DB
		departmentService.saveDepartments(list);
	}

	// Final upload data
	public boolean doEmployeeDataUpload(List<Employee> list) {
		return employeeService.saveOrUpdateEmployee(list);
	}
}
