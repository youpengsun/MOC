package com.sap.moc.service;

import java.util.List;
import com.sap.moc.vo.Pager;
import com.sap.moc.entity.Employee;
import com.sap.moc.vo.Ordering;


public interface IEmployeeService {

	//-------------------------- for Employee----------------------------------
	
	//for query page
	public Pager<Employee> getEmployeeByPage(Employee ee, Pager<Employee> page , Ordering orders);
	
	//for create , update, delete
	public boolean saveEmployee(Employee ee);
	public boolean saveOrUpdateEmployee(List<Employee> list);
	public boolean updateEmployee(Employee ee);
	public boolean deleteEmployee(Employee ee);
	public boolean updateEmployees(List<Employee> employees);
	// for query
	public List<Employee> getAllEmployee();
	public List<Employee> getAllActiveEmployee();
	public Employee getEmployeeByKey(String id);
	
	// for fuzzy query
	public List<Employee> getEmployee(Employee ee);
	
}
