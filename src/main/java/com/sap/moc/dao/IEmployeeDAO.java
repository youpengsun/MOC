package com.sap.moc.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.sap.moc.entity.Employee;
import com.sap.moc.vo.Ordering;
import com.sap.moc.vo.Pager;

public interface IEmployeeDAO extends IGenericDAO<Employee, String>{
//    public List<Employee> getAll();
//
//    public Employee findByKey(String id);
//
//    public boolean create(Employee employee);    
//
//    public boolean saveOrUpdate(Employee employee);
//    
//    public boolean update(Employee employee);
//    
//    public void saveOrUpdateAll(List<Employee> employees);
//    
//    public boolean delete(Employee employee);  

	public Pager<Employee> queryEmployeeByPage(Employee ee, Pager<Employee> page, Ordering order);
	

	public List<Employee> queryEmployee( Employee ee);
	
	
	public List<Criterion> generateCriterions(Employee ee);
	public List<Order> generateOrder(Ordering ordering);
	  
	
}
