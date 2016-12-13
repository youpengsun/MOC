package com.sap.moc.service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.sap.moc.entity.Vendor;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.test.BaseJunit4Test;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.vo.VendorWithContract;

public class VendorServiceTest extends BaseJunit4Test {
	@Autowired
	IVendorService vendorService;
	@Autowired
	IEmployeeService employeeService;
	@Autowired
	IConsumeService consumeService;

	@SuppressWarnings("deprecation")
	@Test
	@Rollback(false)
	public void testGetAllVendor() {
		Vendor vendor = vendorService.getVendorByKey(10012);
		VendorLine vl = new VendorLine();
		vl.setVendor(vendor);
		vl.setName("new test");
		boolean status = vendorService.saveVendorLineWOLineNo(vl);
		System.out.println("result " +  status);
		System.out.println("ID " +  vl.getId());
		System.out.println("LIne " +  vl.getLineNO());
//		Calendar cal = CommonUtil.getCurrentCalendar();
//		String string = cal.getTime().toString();
//		System.out.println("Current time is " + string + "/ntime zone is:" + cal.getTimeZone());
//		
//		cal.setTimeZone(TimeZone.getTimeZone("GMT+09:00"));
//		System.out.println("Current time is " + cal.getTime().toString() + "/ntime zone is:" + cal.getTimeZone());
		
//		List<Vendor> vendors = vendorService.getActiveVendors();
//		if (vendors.size() > 0){
//			System.out.println("---------------");
//			for(Vendor ve: vendors){
//				System.out.println(ve.getId() + "  " + ve.getName() + ve.getReportPeriod());
//				System.out.println(ve.toString());
//			}
//		}
	}
/*
	@Test
	@Rollback(false)
	public void testSaveVendor() {
		Vendor ve = new Vendor();
		ve.setId(1001);
		ve.setName("Baker&Spice");
		ve.setWechatID("wechatid");
		vendorService.saveVendor(ve);
		List<Vendor> list = vendorService.getAllVendor();
		System.out.println("Vendors:");
		for (Vendor v : list) {
			System.out.println(v.getId() + "-" + v.getName() + "-" + v.getWechatID());
		}
	}

	@Test
	@Rollback(false)
	public void testSaveVendorLine() {
		VendorLine vl = new VendorLine();
		vl.setId(1);
		vl.setLineNO(1);
		vl.setName("A Line");
		vl.setVendor(vendorService.getVendorByKey(2));
		vl.setWechatID("wechatit");
		vendorService.saveVendorLine(vl);

	}

	@Test
	public void testGetContractByVendorId() {
		fail("Not yet implemented");
	}

	@Test
	@Rollback(false)
	public void testGetTodayConsumeRecord() {
		
		for(Employee ee: employeeService.getAllEmployee()){
			System.out.println(ee.getId() + ee.getFirstName());
			
		}
		consumeService.saveConsumeRecord("I074184", "2-1");
		System.out.println("read consume record");

		List<ConsumeRecord> list = consumeService.getTodayConsumeRecord("I074184", IConsumeService.SUCCESS, "");
		System.out.println(list.size());
		for (ConsumeRecord cr : list) {
			System.out.println(cr.getId() + " " + cr.getTransactionCode() + " " + cr.getEmployee().getId() + " "
					+ cr.getVendorLine());
		}

	}*/
	@Test
	public void testGetActiveVendorList(){
		List<Vendor> vendors = vendorService.getActiveVendors();
		Collections.sort(vendors,new Comparator<Vendor>(){  
            public int compare(Vendor v1, Vendor v2) {  
                return v1.getBusinessDistrict().getId().compareTo(v2.getBusinessDistrict().getId());  
            }  
        });  
		
		for (Vendor vendor : vendors) {
			System.out.print(vendor.getBusinessDistrict().getId());
			System.out.println(vendor.getBusinessDistrict().getDescription());
		}
	}

}
