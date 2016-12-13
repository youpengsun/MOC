package com.sap.moc.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.sap.moc.entity.Department;
import com.sap.moc.entity.Employee;
import com.sap.moc.exception.AdminException;
import com.sap.moc.exception.WeChatException;
import com.sap.moc.service.IDepartmentService;
import com.sap.moc.service.IEmployeeBatchUploadService;
import com.sap.moc.service.IEmployeeService;
import com.sap.moc.service.ISyncDepartmentsService;
import com.sap.moc.service.ISyncEmployeesService;
import com.sap.moc.service.IValidateService;
import com.sap.moc.utils.ConstantReader;
import com.sap.moc.utils.TokenUtil;
import com.sap.moc.vo.DepartmentSync;
import com.sap.moc.vo.EmployeeUpload;
import com.sap.moc.vo.Ordering;
import com.sap.moc.vo.Pager;
import com.sap.moc.vo.UIEmployeeSync;

@Controller
public class AdminController {
	class NullHostNameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}

	}

	@Autowired
	private IDepartmentService departmentService;

	@Autowired
	private IEmployeeService employeeService;

	@Autowired
	private IEmployeeBatchUploadService employeeBatchUploadService;

	@Autowired
	IValidateService validateEE;

    @Autowired
    ISyncEmployeesService syncEmployees;

    @Autowired
    ISyncDepartmentsService syncDepartmentsService;
    
	@RequestMapping("/adminctrl/emp/list")
	public @ResponseBody Map<String, List<Employee>> getEmployeeList() {
		Map<String, List<Employee>> map = new HashMap<String, List<Employee>>();
		map.put("employees", employeeService.getAllEmployee());
		return map;
	}

	@RequestMapping("/adminctrl/dept/list")
	public @ResponseBody Map<String, List<Department>> getDepartmentList() {
		Map<String, List<Department>> map = new HashMap<String, List<Department>>();
		map.put("departments", departmentService.getAllDepartment());
		return map;
	}

	@RequestMapping("/adminctrl/emp/query")
	public @ResponseBody Map<String, List<Employee>> getEmployeeQuery(@RequestBody Employee employee) {
		Map<String, List<Employee>> map = new HashMap<String, List<Employee>>();
		map.put("employees", employeeService.getEmployee(employee));
		return map;
	}

	@RequestMapping("/adminctrl/emp/save/{sync}")
	public @ResponseBody Map<String, Object> save(@RequestBody Employee employee, @PathVariable("sync") String sync) {
		Map<String, Object> map = new HashMap<String, Object>();
		String errorMSG = validateEE.validateEmployeeData(employee, EmployeeUpload.insert,EmployeeUpload.MODE_SINGLE);
		if (!errorMSG.isEmpty()) {
			map.put("create_status", false);
			map.put("create_message", errorMSG);
		} else {
			boolean createEmployee = employeeService.saveEmployee(employee);
			map.put("create_status", createEmployee);
			// for Sync with Wechat
			if (sync.equalsIgnoreCase("yes")) { // Sync checkbox marked
				if (createEmployee == true) {
					List<Employee> eeList = new ArrayList<Employee>();
					eeList.add(employee);
					try {
						List<UIEmployeeSync> ee_wechat_to = syncEmployees.checkEmployees(eeList);
						List<UIEmployeeSync> ee_wechat_back = syncEmployees.updateEmployees(ee_wechat_to);
						map.put("sync_status", ee_wechat_back.get(0).getUpdateStatus());
						map.put("sync_message", ee_wechat_back.get(0).getComments());
					} catch (Exception e) {
						map.put("sync_status", false);
						map.put("sync_message", e.getMessage());
					}
				}
			}
		}
		return map;
	}

	@RequestMapping("/adminctrl/emp/edit/{sync}")
	public @ResponseBody Map<String, Object> edit(@RequestBody Employee employee, @PathVariable("sync") String sync) {
		Map<String, Object> map = new HashMap<String, Object>();
		String errorMSG = validateEE.validateEmployeeData(employee, EmployeeUpload.update,EmployeeUpload.MODE_SINGLE);
		if (!errorMSG.isEmpty()) {
			map.put("edit_status", false);
			map.put("edit_message", errorMSG);
		} else {
			boolean editEmployee = employeeService.updateEmployee(employee);
			map.put("edit_status", editEmployee);
			if (sync.equalsIgnoreCase("yes")) { // Sync checkbox marked
				if (editEmployee == true) {
					List<Employee> eeList = new ArrayList<Employee>();
					eeList.add(employee);
					try {
						List<UIEmployeeSync> ee_wechat_to = syncEmployees.checkEmployees(eeList);
						List<UIEmployeeSync> ee_wechat_back = syncEmployees.updateEmployees(ee_wechat_to);
						map.put("sync_status", ee_wechat_back.get(0).getUpdateStatus());
						map.put("sync_message", ee_wechat_back.get(0).getComments());
					} catch (Exception e) {
						map.put("sync_status", false);
						map.put("sync_message", e.getMessage());
					}
				}
			}
		}
		return map;
	}

	@RequestMapping("/adminctrl/emp/del/{id}")
	public @ResponseBody Map<String, Boolean> del(@PathVariable("id") String id) {
		Employee ee = new Employee();
		ee.setId(id);
		boolean deleteEmployee = employeeService.deleteEmployee(ee);
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("status", deleteEmployee);
		return map;
	}

	@RequestMapping("/adminctrl/emp/page/{id}")
	public Pager<Employee> getPage(@PathVariable("id") Integer id, @RequestBody Employee employee) {
		Pager<Employee> page = new Pager<Employee>(id, 10);
		Ordering orders = new Ordering();
		page = employeeService.getEmployeeByPage(employee, page, orders);
		return page;
	}

	@RequestMapping("/adminctrl/emp/changestatus/{sync}")
	public @ResponseBody Map<String, Object> changeEmployeeStatus(@RequestBody List<Employee> eeList,
			@PathVariable("sync") String sync) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean res = false;
		if (!eeList.isEmpty()) {
			res = employeeService.updateEmployees(eeList);
		}
		map.put("change_status", res);
		// Sync checkbox marked
		if (sync.equalsIgnoreCase("yes")) {
			if (res == true) {
				int fail_num = 0;
				int succ_num = 0;
				List<UIEmployeeSync> ee_wechat_back;
				List<UIEmployeeSync> ee_wechat_to = syncEmployees.checkEmployees(eeList);
				ee_wechat_back = syncEmployees.updateEmployees(ee_wechat_to);
				for (UIEmployeeSync weuser : ee_wechat_back) {
					if (weuser.getUpdateStatus() == false) {
						fail_num++;
					} else {
						succ_num++;
					}
				}
				map.put("sync_success", succ_num);
				map.put("sync_fail", fail_num);
			}

		}
		return map;
	}

	@RequestMapping("/adminctrl/emp/excelupload")
	public @ResponseBody Map<String, Object> excelUpload(@RequestParam("eeexcel") MultipartFile eeexcel) {
		StringBuilder msg = new StringBuilder();
		List<EmployeeUpload> uploadEE = employeeBatchUploadService.handleExcelUpload(eeexcel, msg);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uploadEE", uploadEE);
		map.put("message", msg.toString());
		return map;
	}

	// eeList
	@RequestMapping("/adminctrl/emp/excelsave")
	public @ResponseBody Map<String, Object> excelSave(@RequestBody List<Employee> eeList) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean res = false;
		if (!eeList.isEmpty()) {
			res = employeeBatchUploadService.doEmployeeDataUpload(eeList);
		}
		map.put("status", res);
		return map;
	}

	/**
	 * synchronize employee
	 * @throws Exception 
	 */
	@RequestMapping("/adminctrl/sync/searchall")
	public @ResponseBody Map<String, Object> searchAll() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder message = new StringBuilder();
		map.put("results", syncEmployees.checkAllEmployees(message));
		map.put("message", message.toString());
		return map;
	}
	
	@RequestMapping("/adminctrl/sync/query")
	public @ResponseBody Map<String, List<UIEmployeeSync>> getSyncEmployeeQuery(@RequestBody Employee employee)
			throws Exception {
		Map<String, List<UIEmployeeSync>> map = new HashMap<String, List<UIEmployeeSync>>();
		List<Employee> employees = employeeService.getEmployee(employee);
		List<UIEmployeeSync> results = new ArrayList<UIEmployeeSync>();
		if (!employees.isEmpty()) {
			results = syncEmployees.checkEmployees(employees);
		}
		map.put("results", results);
		return map;
	}

	@RequestMapping("/adminctrl/sync/update")
	public @ResponseBody Map<String, List<UIEmployeeSync>> updateEmployees(
			@RequestBody List<UIEmployeeSync> toSyncEmployees) throws Exception {
		Map<String, List<UIEmployeeSync>> map = new HashMap<String, List<UIEmployeeSync>>();
		map.put("results", syncEmployees.updateEmployees(toSyncEmployees));
		return map;
	}

	@RequestMapping("/adminctrl/wechatdepartment/searchall")
	public @ResponseBody Map<String, List<DepartmentSync>> searchAllDepartment() throws Exception {
		Map<String, List<DepartmentSync>> map = new HashMap<String, List<DepartmentSync>>();
		map.put("results", syncDepartmentsService.checkAllDepartments());
		return map;
	}

	@RequestMapping("/adminctrl/wechatdepartment/update")
	public @ResponseBody Map<String, List<DepartmentSync>> updateDepartments(
			@RequestBody List<DepartmentSync> toSyncDepartments) throws Exception {
		Map<String, List<DepartmentSync>> map = new HashMap<String, List<DepartmentSync>>();
		map.put("results", syncDepartmentsService.updateDepartments(toSyncDepartments));
		return map;
	}

	/**
	 * authorization vendor
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/authorization")
	public void authorization(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ignore ssl authorization
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };
		HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
		SSLContext sc;
		try {
			sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setContentType("text/html;charset=UTF-8");
		String code = request.getParameter("code");
		if (code == null || (code != null && code.length() == 0)) {
			// throw new AdminException(ErrorCode.WITHOUT_VENDOR, "认证失败");
			response.getWriter().print("身份认证失败");
			return;
		}
		String state = request.getParameter("state"); // redirect URL
		// Gson to parse json
		Gson gson = new Gson();
		String accessToken = TokenUtil.getAccessToken();
		String url = ConstantReader.readByKey("GET_USER_ID").replace("&1", accessToken).replace("&2", code);
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");
		InputStream inputStream = connection.getInputStream();
		String string = IOUtils.toString(inputStream);
		ResultBean fromJson2 = gson.fromJson(string, ResultBean.class);
		if (fromJson2 != null && fromJson2.getUserId() != null && fromJson2.getUserId().length() > 0) {
			// added to session
			request.getSession().setAttribute("UserId", fromJson2.getUserId());
			//request.getSession().setMaxInactiveInterval(30);
			// redirect to authorization page
			String requestedURL = request.getRequestURL().toString();
			String servlet_path = request.getServletPath();
			@SuppressWarnings("deprecation")
			String finalURL = requestedURL.replaceAll(servlet_path, URLDecoder.decode(state));
			response.sendRedirect(finalURL);
		} else {
			// throw new AdminException(ErrorCode.AUTHORIZATION_ERROR, "验证失败");
			response.getWriter().print("身份认证失败");
			return;
		}
	}

	@ExceptionHandler(Exception.class)
	public String handleException(Exception exception) {
		// handle exception
		exception.printStackTrace();
		return "error";
	}

	@ExceptionHandler(WeChatException.class)
	public String handleAdminException(AdminException exception) {
		// handle exception
		return "error";
	}

	class ResultBean {
		private String UserId;
		private String DeviceId;

		public String getUserId() {
			return UserId;
		}

		public void setUserId(String userId) {
			UserId = userId;
		}

		public String getDeviceId() {
			return DeviceId;
		}

		public void setDeviceId(String deviceId) {
			DeviceId = deviceId;
		}
	}
	
	@RequestMapping("/mobile/pagemapping/{page}")
	public String handlePageMapping(HttpServletRequest request, HttpServletResponse response, @PathVariable("page") String page) {		
		String requestedURL = request.getRequestURL().toString();
		String servlet_path = request.getServletPath();
		String finalPath = "";
		String destPath = "";
		switch (page) {
		case "monthlysettlement":
			destPath = "/mobile/vendor/monthlysettlement.html";
			break;
		case "dailyrecords":
			destPath = "/mobile/employee/dailyrecords.html";
			break;
		default:
			destPath = servlet_path;
			break;
		}
		finalPath = requestedURL.replace(servlet_path, destPath);
		if (!finalPath.isEmpty()) {
			try {
				response.sendRedirect(finalPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
