package com.sap.moc.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.moc.dao.IDepartmentDAO;
import com.sap.moc.service.impl.SyncDepartmentsServiceImpl;
import com.sap.moc.test.BaseJunit4Test;
import com.sap.moc.vo.WechatDepartment;
import com.sap.moc.vo.WechatDepartmentList;
import com.sap.moc.vo.WechatResult;

public class SyncDepartmentsTest extends BaseJunit4Test {
	@Autowired
	IDepartmentDAO dao;

	@Test
	public void testCheckAllDepartments() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateDepartments() {
		fail("Not yet implemented");
	}

	// @Test
	public void testWechatGetDepartmentList() throws Exception {
		SyncDepartmentsServiceImpl sync = new SyncDepartmentsServiceImpl();
		WechatDepartmentList departList = sync.wechatGetDepartmentList("2");
		for (WechatDepartment department : departList.getDepartment()) {
			System.out.println(department.getName());
		}
	}

	@Test
	public void testWechatCreateDepartment() throws Exception {
		SyncDepartmentsServiceImpl sync = new SyncDepartmentsServiceImpl();
		WechatDepartment wechatDepartment = new WechatDepartment();
		wechatDepartment.setId("1002");
		wechatDepartment.setName("test add");
		wechatDepartment.setParentid("2");
		WechatResult result = sync.wechatCreateDepartment(wechatDepartment);
		System.out.println("Wechat Create Department: " + result.getErrmsg());
	}

	// @Test
	public void testWechatUpdateDepartment() throws Exception {
		SyncDepartmentsServiceImpl sync = new SyncDepartmentsServiceImpl();
		WechatDepartment wechatDepartment = new WechatDepartment();
		wechatDepartment.setId("138011101");
		wechatDepartment.setName("GS CI HCM CN");
		wechatDepartment.setParentid("2");
		WechatResult result = sync.wechatUpdateDepartment(wechatDepartment);
		System.out.println("Wechat Update Department: " + result.getErrmsg());
		assertNotNull(result.getErrmsg());
	}

	@Test
	public void testWechatDeleteDepartment() throws Exception {
		// SyncDepartments sync = new SyncDepartments();
		// WechatResult result = sync.wechatDeleteDepartment("1001");
		// System.out.println("Wechat Delete Department:" + result.getErrmsg());
		// assertEquals(result.getErrcode(), "0");
	}
}
