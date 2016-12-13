package com.sap.moc.service;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.moc.test.BaseJunit4Test;
import com.sap.moc.utils.ConstantReader;
import com.sap.moc.vo.QueryTime;
import com.sap.moc.vo.VendorConsume;

public class AnalysisServiceTest extends BaseJunit4Test {
	@Autowired
	private IAnalysisService service;

	@Test
	public void testAnalysis() throws ParseException {
		QueryTime begmo = new QueryTime(2015, 12, 0);
		QueryTime endmo = new QueryTime(2016, 3, 0);

		List<VendorConsume> list = service.getAnalysisResource(begmo, endmo);

		for (VendorConsume ccc : list) {
			System.out.print("id:" + ccc.getId());
			System.out.print("name:" + ccc.getName());

			System.out.println();
			Map<String, Integer> map = ccc.getDataList();

			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			}
			System.out.println();
		}
		// List<ConsumeRecord> records = query.list();

		assertEquals(list.size(), 3);

	}

	@Test
	public void mytest() {
		String test = ConstantReader.readSysParaByKey("TEST_MODE");

		if (!test.equals("1"))
		{
			
		}
		else {
		}
		assertEquals(test.equals("1"), true);
	}

}
