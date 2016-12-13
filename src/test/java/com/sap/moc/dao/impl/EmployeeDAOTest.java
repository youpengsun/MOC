package com.sap.moc.dao.impl;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.moc.dao.IEmployeeDAO;
import com.sap.moc.entity.Department;
import com.sap.moc.entity.Employee;
import com.sap.moc.test.BaseJunit4Test;

public class EmployeeDAOTest extends BaseJunit4Test {

	@Autowired
	private IEmployeeDAO eeDAO;
	
//	@Test
//	public void testQueryEmployeeByCriteria() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testQueryEmployeePageByCriteria() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testGetEmployeeCountByCriteria() {
		Employee ee = new Employee();
		Department dpt = new Department("111","test");
		ee.setDepartment(dpt);
		ee.setFirstName("Wen");
		List<Employee> list = eeDAO.queryEmployee(ee);
		System.out.println(list.size());
		System.out.println("finish");
		
	}

//	@Test
//	public void testGenerateCriterions() {
//		fail("Not yet implemented");
//	}

//	@Test
//	@Rollback(false)
//	public void testCreate() {
//		Employee ee = new Employee();
//		ee.setId("I074184");
//		ee.setFirstName("Shu");
//		ee.setLastName("Wen");
//		ee.setEmail("shu.wen@sap.com");
//		System.out.println(eeDAO.create(ee));
//
//		System.out.println(ee.getId());
//		assertNotNull(ee.getId());
//	}
//
//	@Test
//	public void testGetAll() {
//		List<Employee> employees = eeDAO.getAll();
//		for(Employee ee: employees){
//			System.out.println(ee.getId());
//		}
//	}

}
