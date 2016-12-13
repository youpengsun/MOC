package com.sap.moc.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.sap.moc.dao.IVendorDAO;
import com.sap.moc.entity.Vendor;
import com.sap.moc.test.BaseJunit4Test;

public class VendorDAOTest extends BaseJunit4Test {
	@Autowired
	private IVendorDAO vendorDAO;

	 @Test
	 @Rollback(false)
	 public void teXstCreate() {
	 Vendor vendor = new Vendor();
	 vendor.setName("vendor test");
	 System.out.println("Creat:" + vendorDAO.create(vendor));
	 System.out.println(vendor.getId());
	 }

//	@Test
//	public void testGetActiveVendors() {
//		List<Vendor> activeVendors = vendorDAO.getActiveVendors();
//		
//		
//		List<Vendor> inactiveVendors = vendorDAO.getInactiveVendors();
//
//		List<Vendor> vendors = vendorDAO.getAll();
//		assertEquals(vendors.size(), activeVendors.size() + inactiveVendors.size());
//	}

}
