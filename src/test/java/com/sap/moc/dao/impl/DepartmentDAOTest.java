package com.sap.moc.dao.impl;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.moc.dao.IDepartmentDAO;
import com.sap.moc.entity.Department;
import com.sap.moc.test.BaseJunit4Test;

public class DepartmentDAOTest extends BaseJunit4Test {
	@Autowired
	private IDepartmentDAO dao;
	
	@Test
	public void testGetAll() {
		List<Department> departments = dao.getAll();
		for(Department de: departments){
			if (de != null) {
				System.out.println(de.getId());
			}
		}
	}
}
