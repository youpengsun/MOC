package com.sap.moc.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sap.moc.dao.IDepartmentDAO;
import com.sap.moc.entity.Department;

@Repository
public class DepartmentDAOImpl extends GenericDAOImpl<Department, String> implements IDepartmentDAO {

	@Override
	public boolean saveDepartments(List<Department> departments) {
		boolean returnValue = true;
		for (Department dep : departments) {
			if (create(dep) == false) {
				returnValue = false;
			}
		}
		return returnValue;
	}

	@Override
	public boolean updateDepartments(List<Department> departments) {
		boolean returnValue = true;
		for (Department dep : departments) {
			if (update(dep) == false) {
				returnValue = false;
			}
		}
		return returnValue;
	}


}
