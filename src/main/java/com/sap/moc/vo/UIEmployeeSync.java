package com.sap.moc.vo;

import com.sap.moc.entity.Employee;
import com.sap.moc.utils.ConstantUtil;

public class UIEmployeeSync {
	private Employee employee;
	private WechatUser user;
	private ConstantUtil.syncStatus syncStatus;
	private String comments;
	private boolean updateStatus;

	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public WechatUser getUser() {
		return user;
	}
	public void setUser(WechatUser user) {
		this.user = user;
	}
	public ConstantUtil.syncStatus getSyncStatus() {
		return syncStatus;
	}
	public void setSyncStatus(ConstantUtil.syncStatus syncStatus) {
		this.syncStatus = syncStatus;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public boolean getUpdateStatus() {
		return updateStatus;
	}
	public void setUpdateStatus(boolean updateStatus) {
		this.updateStatus = updateStatus;
	}
	public void updateWechatResult(WechatResult wechatResult) {
		if (ConstantUtil.SUCCESS.equals(wechatResult.getErrcode())) {
			this.setSyncStatus(ConstantUtil.syncStatus.SYNCED);
			this.setComments(wechatResult.getErrmsg());
			this.setUpdateStatus(true);
		} else {
			this.setComments(wechatResult.getErrmsg());
			this.setUpdateStatus(false);
		}
	}
	
}
