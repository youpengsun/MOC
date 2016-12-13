package com.sap.moc.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.sap.moc.entity.Vendor;
import com.sap.moc.vo.Ordering;
import com.sap.moc.vo.Pager;

public interface IVendorDAO extends IGenericDAO<Vendor, Integer> {

	public Pager<Vendor> queryVendorByPage(Vendor ve, Pager<Vendor> page, Ordering orders);

	public List<Vendor> queryVendor(Vendor ve);

	public List<Criterion> generateCriterions(Vendor ve);

	public List<Order> generateOrder(Ordering ordering);

	public boolean checkExistsByName(String vendorName);

	public List<Vendor> findVendorbyName(String vendorName);

	public List<Vendor> getActiveVendors();

	public List<Vendor> getInactiveVendors();
}
