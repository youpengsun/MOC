package com.sap.moc.vo;

import com.sap.moc.entity.Department;
import com.sap.moc.utils.ConstantUtil;

public class DepartmentSync {
	private Department department;
	private WechatDepartment wechatDepartment;
	private ConstantUtil.syncStatus syncStatus;
	private String comments;
	private boolean updateStatus;

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public WechatDepartment getWechatDepartment() {
		return wechatDepartment;
	}

	public void setWechatDepartment(WechatDepartment wechatDepartment) {
		this.wechatDepartment = wechatDepartment;
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

	public boolean isUpdateStatus() {
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
