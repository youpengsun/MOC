package com.sap.moc.service;

import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.moc.entity.Employee;
import com.sap.moc.service.impl.SyncEmployeesServiceImpl;
import com.sap.moc.test.BaseJunit4Test;
import com.sap.moc.utils.ConstantReader;
//import com.sap.moc.utils.HttpUtilTest;
import com.sap.moc.utils.TokenUtil;
import com.sap.moc.vo.WechatResult;
import com.sap.moc.vo.WechatUser;

public class SyncEmployeesTest extends BaseJunit4Test{
	@Autowired
	ISyncEmployeesService sync;
	@Autowired
	IEmployeeService employeeService;

//	@Test
	public void testRandomWechatCreateUser() {
		String DevMode = ConstantReader.readSysParaByKey("DEV_FLAG");
		if (DevMode.equals("1")) {
			System.setProperty("https.proxyHost", "proxy.sha.sap.corp");
			System.setProperty("https.proxyPort", "8080");
		}
		WechatUser user = new WechatUser();
		List<String> departs = new ArrayList<String>();
		departs.add(ConstantReader.readByKey("SAP_DEPARTMENT_ID"));
		departs.add("138011199");
		user.setDepartment(departs);
		user.setMobile("13641763310");
		user.setName("Test HttpUtil");
		user.setUserid("I074193");
		user.setEmail("vitatest.hua@sap.com");

		for (int i = 0; i < 10; i++) {
			String accessToken = TokenUtil.getAccessToken();
			String urlTemp = ConstantReader.readByKey("UPDATE_USER_URL");
			urlTemp = urlTemp.replaceAll("&1", accessToken);
			try {
				URL url = new URL(urlTemp);
				HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Charset", "UTF-8");
				OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				writer.write(mapper.writeValueAsString(user));
				writer.flush();
				InputStream inputStream = connection.getInputStream();
				String input = IOUtils.toString(inputStream);
				System.out.println("No." + i + input);
			} catch (Exception ex) {
				System.out.println("No." + i + ex.getMessage());
			}
		}
	}

	// @Test
	public void testWechatGetEmployee() throws Exception {
		String DevMode = ConstantReader.readSysParaByKey("DEV_FLAG");
		if (DevMode.equals("1")) {
			System.setProperty("https.proxyHost", "proxy.sha.sap.corp");
			System.setProperty("https.proxyPort", "8080");
		}
		WechatUser user;
		user = sync.wechatGetEmployee("I074193");
		System.out.println("Error code:" + user.getErrcode() + " Error message:" + user.getErrmsg());
	}

	// @Test
	public void testWechatCreateUser() throws Exception {
		String DevMode = ConstantReader.readSysParaByKey("DEV_FLAG");
		if (DevMode.equals("1")) {
			System.setProperty("https.proxyHost", "proxy.sha.sap.corp");
			System.setProperty("https.proxyPort", "8080");
		}
		WechatUser user = new WechatUser();
		List<String> departs = new ArrayList<String>();
		departs.add(ConstantReader.readByKey("SAP_DEPARTMENT_ID"));
		departs.add("138011188");
		user.setDepartment(departs);
		user.setMobile("13641763310");
		user.setName("Test HttpUtil");
		user.setUserid("I074193");
		user.setEmail("vitatest.hua@sap.com");

		WechatResult wechatResult = sync.wechatCreateUser(user);

		System.out.println(wechatResult.getErrmsg());
	}

	@Test
	public void testRandomWechatUpdateUser() throws Exception {
//		String DevMode = ConstantReader.readSysParaByKey("DEV_FLAG");
//		if (DevMode.equals("1")) {
//			System.setProperty("https.proxyHost", "proxy.sha.sap.corp");
//			System.setProperty("https.proxyPort", "8080");
//		}
//		WechatUser user = new WechatUser();
//		List<String> departs = new ArrayList<String>();
//		departs.add(ConstantReader.readByKey("SAP_DEPARTMENT_ID"));
//		departs.add("138011188");
//		user.setDepartment(departs);
//		user.setMobile("13641763310");
//		user.setName("Test HttpUtil");
//		user.setUserid("I074193");
//		user.setEmail("vitatest.hua@sap.com");
//		ObjectMapper mapper = new ObjectMapper();
//		HashMap<String, String> param = new HashMap<String, String>();
//		for (int i = 0; i < 10; i++) {
//			String accessToken = TokenUtil.getAccessToken();
//			String urlTemp = ConstantReader.readByKey("UPDATE_USER_URL");
//			urlTemp = urlTemp.replaceAll("&1", accessToken);
//			param.put("Content-Type", "application/json");
//
//			String result = HttpUtilTest.doPost(urlTemp, param, mapper.writeValueAsString(user));
//
//			System.out.println("No." + i + result);
//
//		}
	}

//	@Test
	public void testWechatUpdateUser() {
		fail("Not yet implemented");
	}

//	@Test
	public void testWechatDeleteUser() {
		fail("Not yet implemented");
	}

//	@Test
	public void testWechatBatchSyncUser() throws Exception {
		List<Employee> employees = new ArrayList<Employee>();
		Employee ee = employeeService.getEmployeeByKey("1");
		Employee ee2 = employeeService.getEmployeeByKey("10");
		employees.add(ee);
		employees.add(ee2);
		SyncEmployeesServiceImpl syncService = new SyncEmployeesServiceImpl();
		String csvString = syncService.createEmployeeCSV(employees);
		String mediaId = sync.wechatMeidaUpload(csvString);
		System.out.println("Media Id"+mediaId);
		String result = sync.wechatBatchSyncUser(mediaId);
		System.out.println(result);
		
		fail("Not yet implemented");
	}
	
	@Test
	public void testWechatGetJobResult() throws Exception {
		sync.wechatGetJobResult("bz6BDUUawdnjJANVhfPcdbUuatjLV3L22xRyEtbykAU");
	}

//	@Test
	public void testWechatMeidaUpload() {
		fail("Not yet implemented");
	}

//	@Test
	public void testWechatGetUsersByDepartment() {
		fail("Not yet implemented");
	}

//	@Test
	public void testWechatBatchDeleteUser() {
		fail("Not yet implemented");
	}

}
