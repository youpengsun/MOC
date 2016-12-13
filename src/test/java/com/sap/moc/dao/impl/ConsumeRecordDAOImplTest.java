package com.sap.moc.dao.impl;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.sap.moc.dao.IConsumeRecordDAO;
import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.test.BaseJunit4Test;

public class ConsumeRecordDAOImplTest extends BaseJunit4Test {

	@Autowired
	private IConsumeRecordDAO dao;

	@Test
	@Rollback(false)
	public void testGetTodayConsumeRecord() {
		System.out.println("<TestDAO>");
		List<ConsumeRecord> records = dao.getTodayConsumeRecord("", "", "");

		System.out.println(records.size());;

		for(ConsumeRecord cr: records){
			System.out.println("ID" + cr.getId() + "  TIME:" + cr.getTime() + "  EE:" + cr.getEmployee().getId() + "status" + cr.getStatus());
		}
		System.out.println("<TestDAO>");

	}
//
//	@Test
//	public void testGetConsumeRecordsByVendor() {
//		System.out.println("<testGetConsumeRecordsByVendor>");
//		QueryTime time = new QueryTime(2015, 1, 1);
//		List<ConsumeRecord> records = dao.getConsumeRecordsByVendor(1, time);
//
//		assertEquals(records.size(), 0);
//		System.out.println("</testGetConsumeRecordsByVendor>");
//	}
}
