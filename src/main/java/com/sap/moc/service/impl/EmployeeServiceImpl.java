/**
 * 
 */
package com.sap.moc.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.moc.dao.IEmployeeDAO;
import com.sap.moc.entity.Employee;
import com.sap.moc.service.IEmployeeService;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.vo.Ordering;
import com.sap.moc.vo.Pager;

/**
 * @author I074184
 *
 */
@Service
public class EmployeeServiceImpl implements IEmployeeService {
    
	@Autowired
	private IEmployeeDAO   employeeDAO;
	
	//-----------------------------for employee----------------------------------------------------
	/* 
	 * @fuzzy query by page using the information in ee, and orders
	 */

	@Override
	public Pager<Employee> getEmployeeByPage(Employee ee, Pager<Employee> page, Ordering order) {
		// get employee by page
		return employeeDAO.queryEmployeeByPage(ee, page, order);
	}

	/* 
	 * query all employees
	 */
	@Override
	public List<Employee> getAllEmployee() {
		
		return employeeDAO.getAll();
	}
	/* 
	 * query all active employees
	 */
	@Override
	public List<Employee> getAllActiveEmployee() {
		return employeeDAO.findByProperty("status", ConstantUtil.EMPLOYEE_ACTIVE);		
	}
	
	/* 
	 * fuzzy query for employee
	 */
	@Override
	public List<Employee> getEmployee(Employee ee) {
		
		return employeeDAO.queryEmployee(ee);
	}

	
	// for create, update, and delete
	@Override
	public boolean saveEmployee(Employee ee) {
		return employeeDAO.create(ee);
	}
    
	@Override
	public boolean saveOrUpdateEmployee(List<Employee> list) {
		return employeeDAO.saveOrUpdateAll(list);
	}


	@Override
	public boolean updateEmployee(Employee ee) {
		return employeeDAO.update(ee);
		
	}

	@Override
	public boolean deleteEmployee(Employee ee) {
		return employeeDAO.delete(ee);
		
	}
	
	@Override
	public boolean updateEmployees(List<Employee> employees) {
		boolean returnValue = true;
		for(Employee ee : employees){
			if(employeeDAO.update(ee) == false){
				returnValue = false;
			}
		}
		
		return returnValue;
	}
	
	@Override
	public Employee getEmployeeByKey(String id) {
		return employeeDAO.findByKey(id);
	}


}
