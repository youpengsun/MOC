package com.sap.moc.service;

import java.util.List;

import com.sap.moc.vo.DepartmentSync;
import com.sap.moc.vo.WechatDepartment;
import com.sap.moc.vo.WechatDepartmentList;
import com.sap.moc.vo.WechatResult;

public interface ISyncDepartmentsService {
	public List<DepartmentSync> checkAllDepartments() throws Exception;

	public List<DepartmentSync> updateDepartments(List<DepartmentSync> toSyncDepartment) throws Exception;
	
	public WechatDepartmentList wechatGetDepartmentList(String id) throws Exception;
	
	public WechatResult wechatCreateDepartment(WechatDepartment wechatDepartment) throws Exception;
	
	public WechatResult wechatDeleteDepartment(String id) throws Exception;
	
	public WechatResult wechatUpdateDepartment(WechatDepartment wechatDepartment) throws Exception;
	
	
}
 