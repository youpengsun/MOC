/**
 * 
 */
package com.sap.moc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.moc.dao.IDepartmentDAO;
import com.sap.moc.entity.Department;
import com.sap.moc.service.IDepartmentService;

/**
 * @author I074184
 *
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService {

	@Autowired
	private IDepartmentDAO departmentDAO;

	// -----------------------------for
	// department----------------------------------------------------
	@Override
	public Department getDepartment(String id) {
		return departmentDAO.findByKey(id);
	}

	@Override
	public List<Department> getAllDepartment() {
		return departmentDAO.getAll();
	}

	@Override
	public boolean updateDepartments(List<Department> departments) {

		return departmentDAO.updateDepartments(departments);
	}

	@Override
	public boolean saveDepartments(List<Department> departments) {

		return departmentDAO.saveDepartments(departments);
	}

}
