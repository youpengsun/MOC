package com.sap.moc.dao.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.moc.dao.IVendorLineDAO;
import com.sap.moc.entity.Vendor;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.test.BaseJunit4Test;
import com.sap.moc.vo.QRCode;

public class VendorLineDAOImplTest extends BaseJunit4Test {

	@Autowired
	private IVendorLineDAO dao;
	// @Test
	// public void testGetVendorbyQRCode() {
	//
	// QRCode code = new QRCode("1-1");
	// dao.getVendorbyQRCode(code);
	//
	// }

//	@Test
//	public void testSaveOrUpdate() {
//		Vendor vendor = new Vendor();
//		vendor.setId(10001);
//
//		VendorLine line = new VendorLine();
//		line.setVendor(vendor);
//		line.setName("Pizza Express Line 2");
//		line.setLineNO(2);
//		System.out.println(dao.create(line));
//		assertNotNull(line.getId());
//	}
//
//	@Test
//	public void testGetLineByQRCode() {
//		QRCode qrCode = new QRCode("10010-1");
//		VendorLine line = dao.getLinebyQRCode(qrCode);
//		
//		assertNotNull(line);
//	}
	
	@Test
	public void testGetLineNo() {
		int vendorId = 10012;
		int i = dao.getMaxVendorLineNo(vendorId);
		
		System.out.println("max line no for " + vendorId + "is " + i);
	}
}
