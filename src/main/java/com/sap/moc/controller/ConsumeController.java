package com.sap.moc.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sap.moc.entity.VendorLine;
import com.sap.moc.service.IAnalysisService;
import com.sap.moc.service.IConsumeService;
import com.sap.moc.service.IVendorService;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.utils.SendEmail;
import com.sap.moc.vo.AnalysisRequest;
import com.sap.moc.vo.ConsumeInquiryCriteria;
import com.sap.moc.vo.ConsumeInquiryCriteriaBuild;
import com.sap.moc.vo.ConsumeRecordVO;
import com.sap.moc.vo.QRCode;
import com.sap.moc.vo.QueryTime;
import com.sap.moc.vo.SettlementReport;
import com.sap.moc.vo.SettlementRequest;
import com.sap.moc.vo.VendorConsume;

@Controller
public class ConsumeController {
	@Autowired
	private IConsumeService consumeService;
	@Autowired
	private IAnalysisService analysisService;
	@Autowired
	private IVendorService vendorService;

	@RequestMapping("/adminctrl/settlement/query")
	@ResponseBody
	public Map<String, SettlementReport> querySettlement(@RequestBody SettlementRequest request) {
		Map<String, SettlementReport> map = new HashMap<String, SettlementReport>();
		Date startDate = request.getBeginDate();
		Date endDate = request.getEndDate();
		SettlementReport reportResult = consumeService.getSettlementReport(request.getVendorId(), startDate, endDate);
		map.put("report", reportResult);
		return map;
	}

	@RequestMapping("/adminctrl/vendor/consumeanalysis")
	@ResponseBody
	public Map<String, VendorConsume[]> analysis(@RequestBody AnalysisRequest request) throws Exception {
		Map<String, VendorConsume[]> map = new HashMap<String, VendorConsume[]>();
		QueryTime begmo = request.getBegda();
		QueryTime endmo = request.getEndda();

		List<VendorConsume> list = analysisService.getAnalysisResource(begmo, endmo);

		VendorConsume[] dataSource = list.toArray(new VendorConsume[list.size()]);

		map.put("dataSource", dataSource);
		return map;
	}

//	@RequestMapping("/mobilectrl/consumerecord/querymonth")
//	@ResponseBody
//	public Map<String, SettlementReport> queryMonth(HttpServletRequest request, @RequestParam Date beginDate,
//			@RequestParam Date endDate) throws Exception {
//		String id = (String) request.getSession().getAttribute("UserId");
//		QRCode code = new QRCode(id);
//		int vendorId = code.getVendorId();
//
//		Map<String, SettlementReport> result = new HashMap<String, SettlementReport>();
//		SettlementReport reportResult = consumeService.getSettlementReport(vendorId, beginDate, endDate);
//		result.put("results", reportResult);
//		return result;
//	}
	
	@RequestMapping("/mobilectrl/consumerecord/querymonth")
	@ResponseBody
	public Map<String, Object> queryMonth(HttpServletRequest request, @RequestParam Date beginDate,
			@RequestParam Date endDate) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		List<SettlementReport> records = new ArrayList<SettlementReport>();
		String message = "";
		String id = (String) request.getSession().getAttribute("UserId");
		if (id != null) {
			QRCode code = new QRCode(id);
			int vendorId = code.getVendorId();
			SettlementReport reportResult = consumeService.getSettlementReport(vendorId, beginDate, endDate);
			records.add(reportResult);
		} else {
			message = "当前会话已失效，请重新刷新页面";
		}
		result.put("records", records);
		result.put("message", message);
		return result;
	}

	@RequestMapping("/mobilectrl/consumerecord/sendmonthlydetails")
	@ResponseBody
	public Map<String, String> sendMonthlyDetails(HttpServletRequest request, @RequestParam Date beginDate,
			@RequestParam Date endDate) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		String id = (String) request.getSession().getAttribute("UserId");
		if (id != null) {
			QRCode code = new QRCode(id);
			int vendorId = code.getVendorId();
			SettlementReport settlementReport = consumeService.getSettlementReport(vendorId, beginDate, endDate);
			String fileName = settlementReport.getVendorName() + beginDate + "~" + endDate + "报表明细";
			byte[] excelByte = consumeService.buildExcelOfMonthlySettlement(settlementReport);
			result.put("status", SendEmail.send(settlementReport.getVendorEmail(), fileName, excelByte));
		} else {
			result.put("status", "当前会话已失效，请重新刷新页面");
		}
		return result;
	}

	
	@RequestMapping("/mobilectrl/vendor/dailycount")
	@ResponseBody
	public Map<String, Object> dailyCount(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ConsumeRecordVO> records = new ArrayList<ConsumeRecordVO>();
		String message = "";
		Timestamp ts1 = null;
		Timestamp ts2 = null;
		// get from/to
		String fromtime = request.getParameter("from");
		String totime = request.getParameter("to");
		if (!fromtime.isEmpty() && !totime.isEmpty()) {
			String[] from = fromtime.split("/");
			String[] to = totime.split("/");
			Calendar cal = CommonUtil.getCurrentCalendar();
			cal.set(Integer.parseInt(from[0]), Integer.parseInt(from[1]) - 1, Integer.parseInt(from[2]), 0, 0, 0);
			ts1 = new Timestamp(cal.getTimeInMillis());
			cal.set(Integer.parseInt(to[0]), Integer.parseInt(to[1]) - 1, Integer.parseInt(to[2]), 23, 59, 59);
			ts2 = new Timestamp(cal.getTimeInMillis());
		}
		// get session data
		Object userId = request.getSession().getAttribute("UserId");
		if (userId != null) {
			QRCode code = new QRCode(userId.toString());
			VendorLine line = vendorService.getVendorLinebyQRCode(code);
			// get records
			if (ts1 != null && ts2 != null) {
				records = consumeService.getRecordsByVendorPeriod(line, ConstantUtil.CONSUME_STATUS_SUCCESS, ts1, ts2);
			}						
		}else{
			//Session invalid
			message = "当前会话已失效，请重新刷新页面";			
		}		
		map.put("records", records);
		map.put("message", message);
		return map;
	}	

	@RequestMapping("/adminctrl/consume/inquiry/criteriabuild")
	@ResponseBody
	public Map<String, Object> buildInquiryCriteria() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		ConsumeInquiryCriteriaBuild criteriaBuild;
		criteriaBuild = consumeService.buildInquiryCriteria();

		map.put("criteriaBuild", criteriaBuild);
		return map;
	}

	@RequestMapping("/adminctrl/consume/inquiry")
	@ResponseBody
	public Map<String, Object> consumeInquiry(@RequestBody ConsumeInquiryCriteria criteria) throws ParseException {

		Map<String, Object> map = new HashMap<String, Object>();
		List<ConsumeRecordVO> list = consumeService.inquiryByCriteria(criteria);

		map.put("consumeRecords", list);
		return map; 
	}

	@RequestMapping("/mobilectrl/employee/dailyrecords")
	@ResponseBody
	public Map<String, Object> employeeDailyRecords(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ConsumeRecordVO> records = new ArrayList<ConsumeRecordVO>();
		String message = "";		
		// get session data
		//Object userId = "I074115";		
		Object userId = request.getSession().getAttribute("UserId");
		if (userId != null) {
			try {
				ConsumeInquiryCriteria crt = new ConsumeInquiryCriteria();
				crt.setEmployeeID(userId.toString());
				crt.setStatusKey(ConstantUtil.CONSUME_STATUS_SUCCESS);
				Calendar cal = CommonUtil.getCurrentCalendar();
				crt.setBegda(cal.getTime());
				crt.initializeBegdaTime();
				crt.setEndda(cal.getTime());
				crt.initializeEnddaTime();				
				records = consumeService.inquiryByCriteria(crt);
				if(records.isEmpty()){
					message = "No consumption records found.<br>(今日无消费记录.)";
				}
			} catch (Exception e) {
                message = e.getMessage();
			}
		} else {
			// Session invalid
			message = "Current session invalid, please refresh the page";
		}
		map.put("records", records);
		map.put("message", message);
		return map;
	}
}
