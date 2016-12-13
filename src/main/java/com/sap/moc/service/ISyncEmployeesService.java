package com.sap.moc.service;

import java.util.List;

import com.sap.moc.entity.Employee;
import com.sap.moc.vo.UIEmployeeSync;
import com.sap.moc.vo.WechatBatchResult;
import com.sap.moc.vo.WechatResult;
import com.sap.moc.vo.WechatUser;
import com.sap.moc.vo.WechatUserList;

public interface ISyncEmployeesService {
	public List<UIEmployeeSync> checkAllEmployees(StringBuilder message) throws Exception;

	public List<UIEmployeeSync> checkEmployees(List<Employee> employees) throws Exception;

	public List<UIEmployeeSync> updateEmployees(List<UIEmployeeSync> toSyncEmployees) throws Exception;

	public WechatUser wechatGetEmployee(String employeeId) throws Exception;

	public WechatResult wechatCreateUser(WechatUser user) throws Exception;

	public WechatResult wechatUpdateUser(WechatUser user) throws Exception;

	public WechatResult wechatDeleteUser(String userID) throws Exception;

	public WechatUserList wechatGetUsersByDepartment(String departmentId, boolean fetchChild, String status)
			throws Exception;

	public String wechatBatchSyncUser(String mediaId) throws Exception;

	public WechatBatchResult wechatGetJobResult(String jobId) throws Exception;

	public String wechatMeidaUpload(String csvString);

	public WechatResult wechatBatchDeleteUser(List<Employee> employees) throws Exception;

}
