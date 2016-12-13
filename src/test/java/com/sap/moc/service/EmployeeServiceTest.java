package com.sap.moc.service;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.sap.moc.entity.Employee;
import com.sap.moc.jms.IProducerService;
import com.sap.moc.test.BaseJunit4Test;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.vo.JmsMessage;
import com.sap.moc.vo.Ordering;
import com.sap.moc.vo.Pager;



import static org.junit.Assert.*;

public class EmployeeServiceTest extends BaseJunit4Test {
	
	
	@Autowired
	private IProducerService produceService;
	
	@Test
	public void testSend(){
		JmsMessage message = new JmsMessage("I074184", "Consume fail", "You have consumed successfull","1");
		System.out.println("first message send at------------------------------------"  + CommonUtil.getCurrentTimestamp());
			produceService.sendMessage(message);
		
		try {
			System.out.println("sleep");
			Thread.sleep(100000);
			System.out.println("close");
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
//	@Autowired
//	private IEmployeeService queryService;
//
//	@Test
//	public void testGetAllEmployee() {
//		List<Employee> employees = queryService.getAllEmployee();
//		
//		assertNotEquals(0, employees.size());
//	}

//	@Test
//	public void testQueryEmployeeByPage() {
//		Employee test = new Employee();
//		// test.setFirstName("ShuU");
//		Ordering order = new Ordering();
//		Map<String, Ordering.sort> criteria = new HashMap<String, Ordering.sort>();
//		criteria.put("lastName", Ordering.sort.ASCENDING);
//		order.setSortCriteria(criteria);
//
//		Pager<Employee> page = new Pager<Employee>(3, 5);
//		page.setCurrentPage(2);
//
//		Pager<Employee> returnPage = queryService.getEmployeeByPage(test, page, order);
//		for (Employee ee : returnPage.getEntryList()) {
//			System.out.println("ID:" + ee.getId() + "  Name:" + ee.getFirstName() + " " + ee.getLastName());
//		}
//		System.out.println("");
//	}
//
//	@Test
//	public void testQueryAllEmployee() {
//		System.out.println("This is test for queryAllEmployee----------------------");
//		java.util.List<Employee> list = queryService.getAllEmployee();
//		for (Employee ee : list) {
//			System.out.println("ID:" + ee.getId() + "  Name:" + ee.getFirstName() + " " + ee.getLastName());
//		}
//		System.out.println("");
//	}
//
//	@Test
//	public void testGetAllActiveEmployee() {
//
//		java.util.List<Employee> list = queryService.getAllActiveEmployee();
//		for (Employee ee : list) {
//			System.out.println("ID:" + ee.getId() + "  Name:" + ee.getFirstName() + " " + ee.getLastName());
//		}
//		System.out.println("");
//
//	}
//
//	@Test
//	public void testQueryEmployee() {
//		System.out.println("This is test for queryEmployee----------------");
//		Employee test = new Employee();
//		test.setLastName("Wen");
//
//		java.util.List<Employee> list = queryService.getEmployee(test);
//		for (Employee ee : list) {
//			System.out.println("ID:" + ee.getId() + "  Name:" + ee.getFirstName() + " " + ee.getLastName());
//		}
//		System.out.println("");
//	}
//
//	@Test
//	// @Rollback(false)
//	public void testCreateEmployee() {
//		System.out.println("This is test for create Employee--------------------");
//		Employee test = new Employee();
//		double ran = Math.random();
//		int random = (int) (ran * 1000000);
//		System.out.println(random);
//
//		test.setId("I" + random);
//		test.setFirstName("Shu" + random);
//
//		test.setLastName("Wen" + random);
//		queryService.saveEmployee(test);
//		java.util.List<Employee> list = queryService.getAllEmployee();
//		for (Employee ee : list) {
//			System.out.println("ID:" + ee.getId() + "  Name:" + ee.getFirstName() + " " + ee.getLastName());
//		}
//		System.out.println("");
//
//	}
//
//	@Test
//	// @Rollback(false)
//	public void testUpdateEmployee() {
//		System.out.println("This is test for Update Employee--------------------");
//		java.util.List<Employee> list = queryService.getAllEmployee();
//		for (Employee ee : list) {
//			ee.setFirstName("ShuUpdate");
//			queryService.updateEmployee(ee);
//		}
//
//		testQueryAllEmployee();
//	}
//
//	@Test
//	// @Rollback(false)
//	public void testDeleteEmployee() {
//
//		Employee test = new Employee();
//		test.setId("I074115");
//		test.setFirstName("firstname");
//		test.setLastName("lastname");
//		queryService.saveEmployee(test);
//
//		testQueryAllEmployee();
//		System.out.println("Delete the new created Employee-------------------");
//		queryService.deleteEmployee(test);
//
//	}

}
