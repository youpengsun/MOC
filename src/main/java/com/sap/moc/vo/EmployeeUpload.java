/**
 * 
 */
package com.sap.moc.vo;

import com.sap.moc.entity.Employee;

/**
 * @author I074115
 *
 */
public class EmployeeUpload {
	public static final String insert = "I";
	public static final String update = "U";
	public static final String delete = "D";
	public static final String MODE_BATCH = "MB"; //for Excel Upload
	public static final String MODE_SINGLE = "MS";//Individual Maintain
	public static final String error = "E";
	private Employee employee;
	private String status;
	private String message;
	private int excelRow;

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setExcelRow(int row){
		this.excelRow = row;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public String getStatus() {
		return this.status;
	}

	public String getMessage() {
		return this.message;
	}
	
	public int getExcelRow(){
		return this.excelRow;
	}
}
