package com.sap.moc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.moc.entity.Department;
import com.sap.moc.service.IDepartmentService;
import com.sap.moc.service.ISyncDepartmentsService;
import com.sap.moc.utils.ConstantReader;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.utils.HttpUtil;
import com.sap.moc.utils.TokenUtil;
import com.sap.moc.utils.TrimUtil;
import com.sap.moc.vo.DepartmentSync;
import com.sap.moc.vo.WechatDepartment;
import com.sap.moc.vo.WechatDepartmentList;
import com.sap.moc.vo.WechatResult;

@Service
public class SyncDepartmentsServiceImpl implements ISyncDepartmentsService {
	@Autowired
	private IDepartmentService dao;

	@Override
	public List<DepartmentSync> checkAllDepartments() throws Exception {
		/* get all department from database */
		List<Department> departments = dao.getAllDepartment();

		/* get all wechat departments */
		WechatDepartmentList departList = wechatGetDepartmentList(ConstantReader.readByKey("SAP_DEPARTMENT_ID"));

		/* get compared results */
		List<DepartmentSync> results = new ArrayList<DepartmentSync>();
		if (ConstantUtil.SUCCESS.equals(departList.getErrcode())) {
			results = compareAllDepartments(departments, departList.getDepartment());
		}
		return results;
	}

	public List<DepartmentSync> updateDepartments(List<DepartmentSync> toSyncDepartments) throws Exception {
		if (toSyncDepartments == null || toSyncDepartments.isEmpty()) {
			return toSyncDepartments;
		}
		for (DepartmentSync sync : toSyncDepartments) {
			ConstantUtil.syncStatus status = sync.getSyncStatus();
			if (status == null) {
				continue;
			}
			WechatResult result = new WechatResult();
			WechatDepartment wechatDepartment = new WechatDepartment();
			switch (status) {
			case TOADD:
				wechatDepartment = copyDepartment(sync.getDepartment());
				result = wechatCreateDepartment(wechatDepartment);

				if (ConstantUtil.SUCCESS.equals(result.getErrcode())) {
					sync.setWechatDepartment(wechatDepartment);
				}
				sync.updateWechatResult(result);
				break;
			case TODELETE:
				result = wechatDeleteDepartment(sync.getWechatDepartment().getId());
				sync.updateWechatResult(result);
				break;
			case TOUPDATE:
				wechatDepartment = copyDepartment(sync.getDepartment());
				result = wechatUpdateDepartment(wechatDepartment);
				if (ConstantUtil.SUCCESS.equals(result.getErrcode())) {
					sync.setWechatDepartment(wechatDepartment);
				}
				sync.updateWechatResult(result);
				break;
			default:
				break;
			}
		}
		return toSyncDepartments;
	}

	public WechatDepartmentList wechatGetDepartmentList(String id) throws Exception {
		WechatDepartmentList departList = new WechatDepartmentList();
		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("GET_DEPARTMENT_LIST_URL");
		requestURL = requestURL.replaceAll("&1", accessToken).replaceAll("&2", id);
		String result = HttpUtil.doGet(requestURL);
		if (result != null) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			departList = mapper.readValue(result, WechatDepartmentList.class);
			// remove parent department
			departList.getDepartment().remove(0);
		} else {
			departList.setErrcode(ConstantUtil.CALL_SERVICE_FAILED);
			departList.setErrmsg(ConstantUtil.CALL_SERVICE_FAILED_DESCRIPTION);
		}
		return departList;
	}

	public WechatResult wechatCreateDepartment(WechatDepartment wechatDepartment) throws Exception {
		WechatResult wechatResult = new WechatResult();

		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("CREATE_DEPARTMENT_URL").replaceAll("&1", accessToken);

		Map<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "application/json");
		ObjectMapper mapper = new ObjectMapper();
		String result = HttpUtil.doPost(requestURL, params, mapper.writeValueAsString(wechatDepartment));

		if (result != null) {
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			wechatResult = mapper.readValue(result, WechatResult.class);
		} else {
			wechatResult.setErrcode(ConstantUtil.CALL_SERVICE_FAILED);
			wechatResult.setErrmsg(ConstantUtil.CALL_SERVICE_FAILED_DESCRIPTION);
		}
		return wechatResult;
	}

	public WechatResult wechatDeleteDepartment(String id) throws Exception {
		WechatResult wechatResult = new WechatResult();

		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("DELETE_DEPARTMENT_URL");
		requestURL = requestURL.replaceAll("&1", accessToken).replaceAll("&2", String.valueOf(id));
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

	public WechatResult wechatUpdateDepartment(WechatDepartment wechatDepartment) throws Exception {
		WechatResult wechatResult = new WechatResult();

		String accessToken = TokenUtil.getAccessToken();
		String requestURL = ConstantReader.readByKey("UPDATE_DEPARTMENT_URL");
		requestURL = requestURL.replaceAll("&1", accessToken);
		Map<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "application/json");
		ObjectMapper mapper = new ObjectMapper();
		String result = HttpUtil.doPost(requestURL, params, mapper.writeValueAsString(wechatDepartment));

		if (result != null) {
			wechatResult = mapper.readValue(result, WechatResult.class);
		} else {
			wechatResult.setErrcode(ConstantUtil.CALL_SERVICE_FAILED);
			wechatResult.setErrmsg(ConstantUtil.CALL_SERVICE_FAILED_DESCRIPTION);
		}
		return wechatResult;
	}

	private List<DepartmentSync> compareAllDepartments(List<Department> departments,
			List<WechatDepartment> wechatDepartments) {
		/* construct HashMap for wechat departments */
		HashMap<String, WechatDepartment> map = new HashMap<String, WechatDepartment>();
		if (wechatDepartments != null && !wechatDepartments.isEmpty()) {
			for (WechatDepartment wechatDepartment : wechatDepartments) {
				if (wechatDepartment != null) {
					map.put(wechatDepartment.getId(), wechatDepartment);
				}
			}
		}

		ArrayList<DepartmentSync> result = new ArrayList<DepartmentSync>();
		if (departments != null && !departments.isEmpty()) {
			for (Department department : departments) {

				if (department == null) {
					continue;
				}
				DepartmentSync sync = new DepartmentSync();
				sync.setDepartment(department);

				String departmentId = String.valueOf(TrimUtil.trimNumber(department.getId()));
				WechatDepartment wechatDepartment = map.get(departmentId);
				if (wechatDepartment != null) {
					sync.setSyncStatus(compareDeparmentToWechat(department, wechatDepartment));
					sync.setWechatDepartment(wechatDepartment);

					map.remove(departmentId);
				} else {
					sync.setSyncStatus(ConstantUtil.syncStatus.TOADD);
				}
				result.add(sync);
			}
		}

		for (WechatDepartment department : map.values()) {
			DepartmentSync sync = new DepartmentSync();
			sync.setWechatDepartment(department);
			sync.setSyncStatus(ConstantUtil.syncStatus.TODELETE);
			result.add(sync);
		}
		return result;
	}

	private ConstantUtil.syncStatus compareDeparmentToWechat(Department department, WechatDepartment wechatDepartment) {
		if (department == null || wechatDepartment == null || department.getName() == null) {
			return null;
		}

		if (!department.getName().equals(wechatDepartment.getName())) {
			return ConstantUtil.syncStatus.TOUPDATE;
		} else if (!ConstantReader.readByKey("SAP_DEPARTMENT_ID").equals(wechatDepartment.getParentid().toString())) {
			return ConstantUtil.syncStatus.TOUPDATE;
		} else {
			return ConstantUtil.syncStatus.SYNCED;
		}
	}

	private WechatDepartment copyDepartment(Department department) {
		WechatDepartment wechatDepartment = new WechatDepartment();
		if (department == null) {
			return wechatDepartment;
		}

		String departmentId = String.valueOf(TrimUtil.trimNumber(department.getId()));
		wechatDepartment.setId(departmentId);
		wechatDepartment.setName(department.getName());
		wechatDepartment.setParentid(ConstantReader.readByKey("SAP_DEPARTMENT_ID"));
		return wechatDepartment;
	}
}
