package com.sap.moc.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.test.BaseJunit4Test;
import com.sap.moc.vo.QueryTime;
import com.sap.moc.vo.SettlementReport;

public class ConsumeServiceTest extends BaseJunit4Test {
	@Autowired
	private IConsumeService consumeService;

//	@Test
//	public void testCreateConsumeRecord() {
//		System.out.println("<testCreateConsumeRecord>");
//		ConsumeRecord consumeRecord = createConsumeRecord();
//		System.out.println("Record Id:" + consumeRecord.getId());
//
//		assertNotNull(consumeRecord);
//	}

	@Test
	public void testGetConsumeRecord() {
		System.out.println("<testGetConsumeRecord>");
		ConsumeRecord cr = consumeService.getConsumeRecord("1");
		assertNotNull(cr);
	}

	@Test
	public void testGetSettlementReport() {

		System.out.println("testGetSettlementReport");
		QueryTime time = new QueryTime(2015, 12, 0);
		SettlementReport report = consumeService.getSettlementReport(123, time);
		assertNotNull(report);
		System.out.println(report.getLineRecords().get(0).getLineNO());
		System.out.println("testGetSettlementReport");
	}

//	private ConsumeRecord createConsumeRecord() {
//		String id = "011111";
//		String scanInfo = "1-1";
//		return consumeService.saveConsumeRecord(id, scanInfo);
//	}

	@Test
	public void testVendorLineTodaysCount() {
		int vendorID = 1;
		int count = consumeService.getVendorLineTodayActiveCount(vendorID);
		System.out.println(count);
	}
}
