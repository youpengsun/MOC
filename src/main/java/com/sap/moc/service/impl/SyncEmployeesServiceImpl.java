package com.sap.moc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.moc.entity.Employee;
import com.sap.moc.service.IEmployeeService;
import com.sap.moc.service.ISyncEmployeesService;
import com.sap.moc.utils.ConstantReader;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.utils.CreateCSVUtil;
import com.sap.moc.utils.HttpUtil;
import com.sap.moc.utils.TokenUtil;
import com.sap.moc.utils.TrimUtil;
import com.sap.moc.vo.UIEmployeeSync;
import com.sap.moc.vo.WechatBatchResult;
import com.sap.moc.vo.WechatBatchSyncUser;
import com.sap.moc.vo.WechatResult;
import com.sap.moc.vo.WechatUser;
import com.sap.moc.vo.WechatUserList;

import net.sf.json.JSONObject;

@Service
public class SyncEmployeesServiceImpl implements ISyncEmployeesService {

	@Autowired
	private IEmployeeService employeeService;

	public static final String CRLF = "\r\n";

	public List<UIEmployeeSync> checkAllEmployees(StringBuilder message) throws Exception {
		/* get all employees from database */
		List<Employee> employees = employeeService.getAllEmployee();
		/* get all users from wechat */
		WechatUserList activeUserList = wechatGetUsersByDepartment(ConstantReader.readByKey("SAP_DEPARTMENT_ID"), true,
				"0");
		WechatUserList inactiveUserList = wechatGetUsersByDepartment(ConstantReader.readByKey("RECYCLE_BIN_ID"), true,
				"0");

		/* compared all employees with all users */
		List<UIEmployeeSync> results = new ArrayList<UIEmployeeSync>();
		if (ConstantUtil.SUCCESS.equals(activeUserList.getErrcode())
				&& ConstantUtil.SUCCESS.equals(inactiveUserList.getErrcode())) {
			List<WechatUser> userList = new ArrayList<>();
			userList.addAll(activeUserList.getUserlist());
			userList.addAll(inactiveUserList.getUserlist());
			results = compareAllEmployees(userList, employees);
		} else {
			message.append(ConstantUtil.CALL_SERVICE_FAILED_DESCRIPTION);
		}
		return results;
	}

	/**
	 * Fetch user from Wechat according to employee's ID, then compare status
	 * between employee and the fetched user return the compared List
	 * 
	 * @param employees
	 *            employees from back-end database
	 * @return the compared List result including user, employee and check
	 *         status
	 * @throws Exception
	 */
	public List<UIEmployeeSync> checkEmployees(List<Employee> employees) throws Exception {
		ArrayList<UIEmployeeSync> result = new ArrayList<UIEmployeeSync>();
		UIEmployeeSync eeSync = null;
		if (employees == null) {
			return result;
		}

		for (Employee employee : employees) {
			eeSync = new UIEmployeeSync();
			eeSync.setEmployee(employee);
			WechatUser wechatUser = wechatGetEmployee(employee.getId());
			if (ConstantUtil.SUCCESS.equals(wechatUser.getErrcode())) {
				eeSync.setSyncStatus(compareEmployeeToUser(employee, wechatUser));
				eeSync.setUser(wechatUser);
			} else if (ConstantUtil.CALL_SERVICE_FAILED.equals(wechatUser.getErrcode())) {
				eeSync.setComments(wechatUser.getErrmsg());
			} else {
				if (ConstantUtil.EMPLOYEE_ACTIVE.equals(employee.getStatus())) {
					eeSync.setSyncStatus(ConstantUtil.syncStatus.TOADD);
				} else if (ConstantUtil.EMPLOYEE_INACTIVE.equals(employee.getStatus())) {
					eeSync.setSyncStatus(ConstantUtil.syncStatus.SYNCED);
				}
			}
			result.add(eeSync);
		}
		return result;
	}

	public List<UIEmployeeSync> updateEmployees(List<UIEmployeeSync> toSyncEmployees) throws Exception {
		for (UIEmployeeSync eeSync : toSyncEmployees) {
			ConstantUtil.syncStatus status = eeSync.getSyncStatus();
			if (status == null) {
				continue;
			}
			WechatResult result = new WechatResult();
			WechatUser user = new WechatUser();
			switch (status) {
			case TOADD:
				user = copyUserFromEmployee(eeSync.getEmployee());
				result = wechatCreateUser(user);
				if (ConstantUtil.SUCCESS.equals(result.getErrcode())) {
					eeSync.setUser(user);
				}
				eeSync.updateWechatResult(result);
				break;
			case TODELETE:
				result = wechatDeleteUser(eeSync.getUser().getUserid());
				eeSync.updateWechatResult(result);
				break;
			case SYNCED:
				eeSync.setUpdateStatus(true);
				break;
			// for TOACTIVE, TODEACTIVE, TOUPDATE
			default:
				user = copyUserFromEmployee(eeSync.getEmployee());
				result = wechatUpdateUser(user);
				if (ConstantUtil.SUCCESS.equals(result.getErrcode())) {
					eeSync.setUser(user);
				}
				eeSync.updateWechatResult(result);
				break;
			}
		}
		return toSyncEmployees;
	}

	/**
	 * Call Wechat service to fetch user from Wechat according to employee's ID
	 * return object WechatUser
	 * 
	 * @param employeeId
	 *            employee's ID
	 * @return object WechatUser
	 * @throws Exception
	 */
	public WechatUser wechatGetEmployee(String employeeId) throws Exception {
		WechatUser user = new WechatUser();
		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("GET_EMPLOYEE_URL");
		requestURL = requestURL.replaceAll("&1", accessToken).replaceAll("&2", employeeId);
		String result = HttpUtil.doGet(requestURL);
		if (result != null) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			user = mapper.readValue(result, WechatUser.class);
		} else {
			user.setErrcode(ConstantUtil.CALL_SERVICE_FAILED);
			user.setErrmsg(ConstantUtil.CALL_SERVICE_FAILED_DESCRIPTION);
		}
		return user;
	}

	/**
	 * Call Wechat service to create user return Wechat error message
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public WechatResult wechatCreateUser(WechatUser user) throws Exception {
		WechatResult wechatResult = new WechatResult();
		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("CREATE_USER_URL");
		requestURL = requestURL.replaceAll("&1", accessToken);
		Map<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "application/json");
		ObjectMapper mapper = new ObjectMapper();
		String result = HttpUtil.doPost(requestURL, params, mapper.writeValueAsString(user));

		if (result != null) {
			wechatResult = mapper.readValue(result, WechatResult.class);
		} else {
			wechatResult.setErrcode(ConstantUtil.CALL_SERVICE_FAILED);
			wechatResult.setErrmsg(ConstantUtil.CALL_SERVICE_FAILED_DESCRIPTION);
		}
		return wechatResult;
	}

	/**
	 * Call Wechat service to update user return Wechat error message
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public WechatResult wechatUpdateUser(WechatUser user) throws Exception {
		WechatResult wechatResult = new WechatResult();

		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("UPDATE_USER_URL");
		requestURL = requestURL.replaceAll("&1", accessToken);
		Map<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "application/json");
		ObjectMapper mapper = new ObjectMapper();
		String result = HttpUtil.doPost(requestURL, params, mapper.writeValueAsString(user));
		if (result != null) {
			wechatResult = mapper.readValue(result, WechatResult.class);
		} else {
			wechatResult.setErrcode(ConstantUtil.CALL_SERVICE_FAILED);
			wechatResult.setErrmsg(ConstantUtil.CALL_SERVICE_FAILED_DESCRIPTION);
		}
		return wechatResult;
	}

	/**
	 * Call Wechat service to delete user from Wechat according to user's ID
	 * return Wechat error code
	 * 
	 * @param userID
	 *            user's ID
	 * @return a string which is the error code from Wechat
	 * @throws Exception
	 */
	public WechatResult wechatDeleteUser(String userID) throws Exception {
		WechatResult wechatResult = new WechatResult();

		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("DELETE_EMPLOYEE_URL");
		requestURL = requestURL.replaceAll("&1", accessToken).replaceAll("&2", userID);
		String result = HttpUtil.doGet(requestURL);
		if (result != null) {
			ObjectMapper mapper = new ObjectMapper();
			wechatResult = mapper.readValue(result, WechatResult.class);
		} else {
			wechatResult.setErrcode(ConstantUtil.CALL_SERVICE_FAILED);
			wechatResult.setErrmsg(ConstantUtil.CALL_SERVICE_FAILED_DESCRIPTION);
		}
		return wechatResult;
	}

	/**
	 * Call Wechat service to fetch users according to department id return a
	 * list of users
	 * 
	 * @param departmentId
	 * @param fetch_child
	 *            whether to fetch child of the department, true - YES, false -
	 *            NO
	 * @param status
	 *            the user's status in Wechat, 0 - All Users; 1 - Followed
	 *            Users; 2 - Forbidden Users; 4 - Followed Users
	 * @return a array list of WechatUser
	 * @throws Exception
	 */
	public WechatUserList wechatGetUsersByDepartment(String departmentId, boolean fetchChild, String status)
			throws Exception {
		WechatUserList userList = new WechatUserList();

		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("GET_USER_LIST_URL");
		requestURL = requestURL.replaceAll("&1", accessToken).replaceAll("&2", departmentId)
				.replaceAll("&3", (fetchChild ? "1" : "0")).replaceAll("&4", status);
		String result = HttpUtil.doGet(requestURL);
		if (result != null) {
			ObjectMapper mapper = new ObjectMapper();
			userList = mapper.readValue(result, WechatUserList.class);
		} else {
			userList.setErrcode(ConstantUtil.CALL_SERVICE_FAILED);
			userList.setErrmsg(ConstantUtil.CALL_SERVICE_FAILED_DESCRIPTION);
		}
		return userList;
	}

	/**
	 * Call Wechat service to batch upload users according to employee's ID
	 * return Wechat error code
	 * 
	 * @param media_id
	 * @return a string which is the error code from Wechat
	 * @throws Exception
	 */
	public String wechatBatchSyncUser(String mediaId) throws Exception {

		/* new object which JSONObject builds from */
		WechatBatchSyncUser object = new WechatBatchSyncUser();
		Map<String, String> callback = new HashMap<String, String>();
		callback.put("url", ConstantReader.readByKey("CALLBACK_URL"));
		callback.put("token", ConstantReader.readSysParaByKey("TOKEN"));
		callback.put("encodingaeskey", ConstantReader.readSysParaByKey("ENCODINGAESKEY"));
		object.setMedia_id(mediaId);
		object.setCallback(callback);
		/* build URL */
		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("BATCH_SYNC_USER_URL");
		requestURL = requestURL.replaceAll("&1", accessToken);

		Map<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "application/json");
		ObjectMapper mapper = new ObjectMapper();
		String wechatResult = HttpUtil.doPost(requestURL, params, mapper.writeValueAsString(object));

		if (wechatResult != null) {
			JSONObject result = JSONObject.fromObject(wechatResult);
			if (ConstantUtil.SUCCESS.equals(result.getString("errcode"))) {
				/*
				 * call Wechat service to get the job result according to job id
				 */
				wechatGetJobResult(result.getString("jobid"));
			}
		}
		return wechatResult;

	}

	/**
	 * Call Wechat service to get job result of batch upload users according to
	 * job id return batch upload result
	 * 
	 * @param jodId
	 * @return object WechatBatchResult which records the details of batch
	 *         upload results
	 * @throws Exception
	 */
	public WechatBatchResult wechatGetJobResult(String jobId) throws Exception {
		WechatBatchResult batchResult = new WechatBatchResult();

		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("GET_RESULT_URL");
		requestURL = requestURL.replaceAll("&1", accessToken).replaceAll("&2", jobId);
		String result = HttpUtil.doGet(requestURL);

		if (result != null) {
			ObjectMapper mapper = new ObjectMapper();
			batchResult = mapper.readValue(result, WechatBatchResult.class);
		} else {
			batchResult.setErrcode(ConstantUtil.CALL_SERVICE_FAILED);
			batchResult.setErrmsg(ConstantUtil.CALL_SERVICE_FAILED_DESCRIPTION);
		}
		return batchResult;

	}

	/**
	 * Call Wechat service to upload media whose type is csv return media id
	 * 
	 * @param csvString
	 *            string of csv file
	 * @return a string which is media id
	 * @throws Exception
	 */
	public String wechatMeidaUpload(String csvString) {
		String boundary = "**********************";
		String twoHyphens = "--";

		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("MEDIA_UPDATE_URL");
		requestURL = requestURL.replaceAll("&1", accessToken).replaceAll("&2", "file");

		Map<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "multipart/form-data;boundary=" + boundary);

		StringBuffer postData = new StringBuffer();
		postData.append(CRLF + twoHyphens + boundary + CRLF);
		postData.append("Content-Disposition: form-data;name=\"media\";filename=\"" + "contacts.csv" + "\"" + CRLF);
		postData.append("content-type:text/csv" + CRLF + CRLF);
		postData.append(csvString);
		postData.append(CRLF + twoHyphens + boundary + twoHyphens + CRLF);

		String result = HttpUtil.doPost(requestURL, params, postData.toString());
		String mediaId = JSONObject.fromObject(result).get("media_id").toString();
		return mediaId;
	}

	/**
	 * Call Wechat service to batch delete users according to employees return
	 * Wechat error code
	 * 
	 * @param employees
	 *            a list of Employee to be deleted
	 * @return a string which is the error code from Wechat
	 * @throws Exception
	 */
	public WechatResult wechatBatchDeleteUser(List<Employee> employees) throws Exception {
		WechatResult wechatResult = new WechatResult();
		/* post data */
		HashMap<String, List<String>> object = new HashMap<String, List<String>>();
		List<String> users = new ArrayList<String>();
		for (Employee e : employees) {
			if (e != null) {
				users.add(e.getId());
			}
		}
		object.put("useridlist", users);
		/* build URL */
		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("BATCH_DELETE_USER_URL");
		requestURL = requestURL.replaceAll("&1", accessToken);
		Map<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "application/json");
		ObjectMapper mapper = new ObjectMapper();
		String result = HttpUtil.doPost(requestURL, params, mapper.writeValueAsString(object));

		if (result != null) {
			wechatResult = mapper.readValue(result, WechatResult.class);
		} else {
			wechatResult.setErrcode(ConstantUtil.CALL_SERVICE_FAILED);
			wechatResult.setErrmsg(ConstantUtil.CALL_SERVICE_FAILED_DESCRIPTION);
		}
		return wechatResult;
	}
	
	/**
	 * Compare statues between employees and users return the compared List
	 * 
	 * @param users
	 *            users from Wechat
	 * @param employees
	 *            employees from back-end database
	 * @return the compared List result including user, employee and check
	 *         status
	 */
	@SuppressWarnings("rawtypes")
	private List<UIEmployeeSync> compareAllEmployees(List<WechatUser> users, List<Employee> employees) {
		/* construct HashMap for users */
		HashMap<String, WechatUser> usersMap = new HashMap<String, WechatUser>();
		if (users != null && !users.isEmpty()) {
			for (WechatUser user : users) {
				if (user != null) {
					usersMap.put(user.getUserid().toUpperCase(), user);
				}
			}
		}

		/* compare employees with users */
		ArrayList<UIEmployeeSync> result = new ArrayList<UIEmployeeSync>();
		if (employees != null && !employees.isEmpty()) {
			for (Employee employee : employees) {
				UIEmployeeSync eeSync = new UIEmployeeSync();
				eeSync.setEmployee(employee);
				WechatUser wechatUser = usersMap.get(employee.getId().toUpperCase());
				/* if employee found in users */
				if (wechatUser != null) {
					eeSync.setSyncStatus(compareEmployeeToUser(employee, wechatUser));
					eeSync.setUser(wechatUser);
					usersMap.remove(employee.getId().toUpperCase());
				/* else employee not found in users */
				} else {
					if (ConstantUtil.EMPLOYEE_ACTIVE.equals(employee.getStatus())) {
						eeSync.setSyncStatus(ConstantUtil.syncStatus.TOADD);
					} else if (ConstantUtil.EMPLOYEE_INACTIVE.equals(employee.getStatus())) {
						eeSync.setSyncStatus(ConstantUtil.syncStatus.SYNCED);
					}
				}
				result.add(eeSync);
			}
		}

		/* users exist in Wechat but non-exist in employees, need to delete */
		for (Iterator iterator = usersMap.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			UIEmployeeSync eeSync = new UIEmployeeSync();
			eeSync.setUser((WechatUser) entry.getValue());
			eeSync.setSyncStatus(ConstantUtil.syncStatus.TODELETE);
			result.add(eeSync);
		}
		return result;
	}

	private ConstantUtil.syncStatus compareEmployeeToUser(Employee employee, WechatUser user) {
		String name = employee.getLastName() + ", " + employee.getFirstName();
		String userStatus = null;
		List<String> department = new ArrayList<String>();
		int departmentId = TrimUtil.trimNumber(employee.getDepartment().getId());
		department.add(String.valueOf(departmentId));
		if (user.getDepartment().contains(ConstantReader.readByKey("RECYCLE_BIN_ID")) && user.getDepartment().size() == 1 ) {
			userStatus = ConstantUtil.EMPLOYEE_INACTIVE;
		} else {
			userStatus = ConstantUtil.EMPLOYEE_ACTIVE;
		}
		try {
			if (!employee.getStatus().equals(userStatus)) {
				if (employee.getStatus().equals(ConstantUtil.EMPLOYEE_ACTIVE)) {
					return ConstantUtil.syncStatus.TOACTIVATE;
				} else if (employee.getStatus().equals(ConstantUtil.EMPLOYEE_INACTIVE)) {
					return ConstantUtil.syncStatus.TODEACTIVATE;
				} else {
					return ConstantUtil.syncStatus.UNKNOWN;
				}
			} else if (ConstantUtil.EMPLOYEE_INACTIVE.equals(employee.getStatus())) {
				return ConstantUtil.syncStatus.SYNCED;
			} else if (!name.equalsIgnoreCase(user.getName()) 
					|| !department.containsAll(user.getDepartment())) {
				return ConstantUtil.syncStatus.TOUPDATE;
			} else if ((employee.getEmail() == null && user.getEmail() != null) 
					|| (employee.getEmail() != null && employee.getEmail().isEmpty() && user.getEmail() != null)
					|| (employee.getEmail() != null && !employee.getEmail().isEmpty() && !employee.getEmail().trim().equalsIgnoreCase(user.getEmail()))) {
				return ConstantUtil.syncStatus.TOUPDATE;
			} else if ((employee.getTelNo() == null && user.getMobile() != null) 
					|| (employee.getTelNo() != null && employee.getTelNo().isEmpty() && user.getMobile() != null )
					|| (employee.getTelNo() != null && !employee.getTelNo().isEmpty() && !employee.getTelNo().trim().equalsIgnoreCase(user.getMobile()))) {
				return ConstantUtil.syncStatus.TOUPDATE;
			} else {
				return ConstantUtil.syncStatus.SYNCED;
			} 
		}catch (Exception ex) {
			System.out.println("Exception" + employee.getId());
			return null;
		}
	}
	
	private WechatUser copyUserFromEmployee(Employee employee) {
		List<String> department = new ArrayList<String>();
		WechatUser user = new WechatUser();
		if (ConstantUtil.EMPLOYEE_ACTIVE.equals(employee.getStatus())) {
			if (employee.getDepartment() != null && !employee.getDepartment().getId().isEmpty()) {
				int departmentId = TrimUtil.trimNumber(employee.getDepartment().getId());
				department.add(String.valueOf(departmentId));
			}
		} else {
			department.add(ConstantReader.readByKey("RECYCLE_BIN_ID"));
		}

		user.setUserid(employee.getId());
		user.setName(employee.getLastName().trim() + ", " + employee.getFirstName().trim());
		if (employee.getTelNo() != null) {
			user.setMobile(employee.getTelNo().trim());
		} else {
			user.setMobile("");
		}
		if (employee.getEmail() != null) {
			user.setEmail(employee.getEmail().trim());
		} else {
			user.setEmail("");
		}
		user.setDepartment(department);
		return user;
	}

	public String createEmployeeCSV(List<Employee> employees) {
		LinkedHashMap<String, String> header = new LinkedHashMap<String, String>();
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

		header.put("name", "姓名");
		header.put("openId", "账号");
		header.put("weChatId", "微信号");
		header.put("telNo", "手机号");
		header.put("email", "邮箱");
		header.put("department", "所在部门");
		header.put("position", "职位");

		for (Employee employee : employees) {
			if (employee != null) {
				HashMap<String, String> dataRow = new HashMap<String, String>();
				dataRow.put("name", employee.getLastName() + employee.getFirstName());
				dataRow.put("openId", employee.getId());
				dataRow.put("weChatId", employee.getWechatId());
				dataRow.put("telNo", employee.getTelNo());
				dataRow.put("email", employee.getEmail());
				dataRow.put("department",
						ConstantReader.readByKey("SAP_DEPARTMENT_ID") + ";" + employee.getDepartment().getId());
				data.add(dataRow);
			}
		}
		return CreateCSVUtil.createCSVFile(data, header);
	}
}
