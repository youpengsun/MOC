package com.sap.moc.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.Order;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


import com.sap.moc.dao.IEmployeeDAO;
import com.sap.moc.entity.Employee;
import com.sap.moc.utils.TrimUtil;
import com.sap.moc.vo.Ordering;
import com.sap.moc.vo.Ordering.sort;
import com.sap.moc.vo.Pager;

@Repository
public class EmployeeDAOImpl extends GenericDAOImpl<Employee, String> implements IEmployeeDAO {

	@Override
	public Pager<Employee> queryEmployeeByPage(Employee ee, Pager<Employee> page, Ordering order) {
		
		return searchByCriteria(page.getCurrentPage(), page.getPageLine(), generateCriterions(ee), generateOrder(order));
	}


	@Override
	public List<Employee> queryEmployee(Employee ee) {

		return searchByCriteria(generateCriterions(ee), new ArrayList<org.hibernate.criterion.Order>());
	}

	@Override
	//generate restrictions for criteria
	// language ignored cause the default value
	public List<Criterion> generateCriterions(Employee ee) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (TrimUtil.trimUtil(ee.getId())) {
			criterions.add(Restrictions.like("id", "%" + ee.getId().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ee.getEmail())) {
			criterions.add(Restrictions.like("email", "%" + ee.getEmail().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ee.getFirstName())) {
			criterions.add(Restrictions.like("firstName", "%" + ee.getFirstName().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ee.getLastName())) {
			criterions.add(Restrictions.like("lastName", "%" + ee.getLastName().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ee.getLocation())) {
			criterions.add(Restrictions.like("location", "%" + ee.getLocation().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ee.getPersonalArea())) {
			criterions.add(Restrictions.like("personalArea", "%" + ee.getPersonalArea().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ee.getPersonalSubarea())) {
			criterions.add(Restrictions.like("personalSubarea", "%" + ee.getPersonalSubarea().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ee.getStatus())) {
			criterions.add(Restrictions.like("status", "%" + ee.getStatus().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ee.getTelNo())) {
			criterions.add(Restrictions.like("telNo", "%" + ee.getTelNo().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ee.getWechatId())) {
			criterions.add(Restrictions.like("wechatId", "%" + ee.getWechatId().trim() + "%"));
		}
		if (ee.getDepartment() != null && TrimUtil.trimUtil(ee.getDepartment().getId())) {
			criterions.add(Restrictions.like("department.id", "%" + TrimUtil.trimNumber(ee.getDepartment().getId()) + "%"));
		}

		return criterions;

	}
    
	@Override
	//using ordering to generate the order list used in criteria
	public List<Order> generateOrder(Ordering ordering){
		List<Order> orderList = new ArrayList<Order>();
		Map<String, sort> criteria  = ordering.getOrderCriteria();
		if( criteria.size()>0) {
			Set<String> set = criteria.keySet();
			for (String key : set) {
				if(criteria.get(key) == sort.ASCENDING){
					 orderList.add(Order.asc(key));
				}
				else if (criteria.get(key) == sort.DESCENDING){
				  orderList.add(Order.desc(key));
				}
			}
        }
		return orderList;
	}

}
