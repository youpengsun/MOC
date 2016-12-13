package com.sap.moc.dao;

import java.util.List;

import com.sap.moc.entity.Department;

public interface IDepartmentDAO extends IGenericDAO<Department, String>{
	public boolean saveDepartments(List<Department> departments);
	public boolean updateDepartments(List<Department> departments);
}
