package com.sap.moc.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.moc.dao.IConsumeRecordDAO;
import com.sap.moc.entity.Vendor;
import com.sap.moc.service.IAnalysisService;
import com.sap.moc.service.IVendorService;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.vo.QueryTime;
import com.sap.moc.vo.VendorConsume;

/**
 * @author I065892
 *
 */
@Service
public class AnalysisService implements IAnalysisService {

	@Autowired
	private IVendorService vendorService;
	@Autowired
	private IConsumeRecordDAO consumeRecordDao;

	@Override
	public List<VendorConsume> getAnalysisResource(QueryTime begmo, QueryTime endmo) throws ParseException {

		// Build vendorConsume List
		@SuppressWarnings("rawtypes")
		List list = consumeRecordDao.getAnalysisResource(begmo, endmo);

		List<VendorConsume> consumeList = buildAnalysisDataList(begmo, endmo);
		// Input Consume record
		for (Object object : list) {
			Object[] line = (Object[]) object;

			addConsumeCount(line, consumeList);
		}

		return consumeList;

	}

	private List<VendorConsume> buildAnalysisDataList(QueryTime begmo, QueryTime endmo) {

		int monthNo = getMonthNo(begmo, endmo);
		// List<Vendor> vendors = vendorDao.getAll();
		List<Vendor> vendors = vendorService.getVendorsWithStatus();
		List<VendorConsume> consumes = new ArrayList<VendorConsume>();

		for (Vendor vendor : vendors) {
			VendorConsume vc = new VendorConsume();
			vc.setId(vendor.getId());
			vc.setName(vendor.getName());
			vc.setVendorType(vendor.getVendorType());
			vc.setActiveStatus(vendor.getActiveStatus());

			Map<String, Integer> dataList = new HashMap<String, Integer>();

			Calendar cal = CommonUtil.getCurrentCalendar();
			cal.set(begmo.getYear(), begmo.getMonth() - 1, 1);

			for (int i = 0; i < monthNo; i++) {
				if (i > 0) {
					cal.add(Calendar.MONTH, 1);
				}
				String y = Integer.toString(cal.get(Calendar.YEAR));
				String m = String.format("%02d", cal.get(Calendar.MONTH) + 1);
				String timeKey = y + m;

				dataList.put(timeKey, 0);
			}
			// Sort by key
			Map<String, Integer> treeMap = new TreeMap<String, Integer>(dataList);
			vc.setDataList(treeMap);
			consumes.add(vc);
		}

		return consumes;
	}

	private void addConsumeCount(Object[] array, List<VendorConsume> consumeList) {
		String vendorId = array[0].toString();
		int year = Integer.parseInt(array[2].toString());
		int month = Integer.parseInt(array[3].toString());
		int count = Integer.parseInt(array[4].toString());

		String timeKey = year + String.format("%02d", month);

		for (VendorConsume vendorConsume : consumeList) {
			String id = Integer.toString(vendorConsume.getId());
			if (id.equals(vendorId)) {
				Map<String, Integer> dataList = vendorConsume.getDataList();
				if (dataList.containsKey(timeKey)) {
					dataList.put(timeKey, count);
				}
			}
		}

	}

	// Get month numbers between two querytime
	private int getMonthNo(QueryTime begmo, QueryTime endmo) {

		int monthNo = 0;
		int gapYear = endmo.getYear() - begmo.getYear();
		monthNo = 12 * gapYear + endmo.getMonth() - begmo.getMonth() + 1;
		return monthNo;
	}

}
