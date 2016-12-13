package com.sap.moc.service;

import com.sap.moc.entity.Employee;
import com.sap.moc.vo.ConsumeValidationResult;

public interface IValidateService {

	// --------------------------for consume
	// record-------------------------------

	// check whether consume can be successful
	public ConsumeValidationResult checkConsume(String employeeId, String scanInfo);

	// //-------------------------- for
	// Employee----------------------------------
	//
	// //check employee is active
	// public boolean checkEmployeeActive(String employeeId);
	//
	// check the employee data is completed
	public String validateEmployeeData(Employee ee, String act,String mode);

	//
	//
	// //-------------------------- for
	// Vendor------------------------------------
	//
	// //check vendor is active
	// public boolean checkVendorActive(int vendorId);
//	public boolean validateVendor(int vendorId);

}
