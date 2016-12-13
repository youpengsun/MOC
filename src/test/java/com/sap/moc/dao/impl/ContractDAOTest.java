package com.sap.moc.dao.impl;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.moc.dao.IContractDAO;
import com.sap.moc.entity.Contract;
import com.sap.moc.test.BaseJunit4Test;

public class ContractDAOTest extends BaseJunit4Test {

	@Autowired
	private IContractDAO contractDAO;

	@Autowired
//	private IVendorDAO vendorDAO;

	// @Test
	// public void TestDAO1() {
	// Date currentDate = new Date(System.currentTimeMillis());
	// System.out.println(currentDate.toString());
	// Vendor ve = vendorDAO.findByKey(2);
	// List<Contract> list = ve.getContractList();
	// System.out.println(list.size());
	// Contract ct = list.get(0);
	// System.out.print(ct.getId());;
	// }

	 @Test
	 public void TestDAO() {
	 System.out.println("<TestDAO>");
	 assertEquals("class com.sap.moc.dao.impl.ContractDAOImpl",
	 this.contractDAO.getClass().toString());
	 System.out.println("</TestDAO>");
	 }

	// @Test
	// @Rollback(false)
	// public void testInsertContract() {
	// Contract contract = new Contract();
	// contract.setComment("test");
	// contract.setBeginDate(new Date(System.currentTimeMillis() -
	// 100000000000l));
	// contract.setEndDate(new Date(System.currentTimeMillis() +
	// 1000000000000l));
	// contract.setVendor(vendorDAO.findByKey(2));
	// contractDAO.create(contract);
	//
	//
	// System.out.println(contract.getId());
	// assertNotNull(contract.getId());
	// }

//	@Test
//	@Rollback(false)
//	public void testDeleteContract() {
//		System.out.println("<testDeleteContract>");
//		Contract ct = new Contract();
//		ct.setId(15);
//		System.out.println(contractDAO.delete(ct));
//		// List<Contract> contracts = contractDAO.getAll();
//		//
//		// for (int i = 0; i < contracts.size(); i++) {
//		// System.out.println(contracts.get(i).getId());
//		// contractDAO.delete(contracts.get(i));
//		// }
//		System.out.println("</testDeleteContract>");
//	}
	//
	// @Test
	// public void contractFindbyKey() {
	// Contract contract = new Contract();
	// contract.setComment("test");
	// contractDAO.create(contract);
	//
	// System.out.println(contract.getId());
	//
	// int id = contract.getId();
	// // Check if the return contract
	// assertNotNull(contractDAO.findByKey(id));
	// }

	@Test
	public void testContractValid() {
		int vendorId = 123;
		Date date = new Date(Calendar.getInstance().getTime().getTime());
		List<Contract> contracts = contractDAO.getCurrentContracts(vendorId, date);

		assertEquals(0, contracts.size());
	}

}