package com.sap.moc.service;

import java.util.List;

import com.sap.moc.entity.Department;

public interface IDepartmentService {

	// -------------------------- for
	// Department------------------------------------
	public Department getDepartment(String id);

	public List<Department> getAllDepartment();

	public boolean saveDepartments(List<Department> departments);

	public boolean updateDepartments(List<Department> departments);
}
