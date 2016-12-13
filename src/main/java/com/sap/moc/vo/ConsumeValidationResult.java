package com.sap.moc.vo;

import java.util.ArrayList;
import java.util.List;

import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.entity.Employee;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.utils.ConstantUtil;

public class ConsumeValidationResult {

	private boolean result;
	private List<ConstantUtil.consumeErrorType> errors;
	private Employee employee;
	private VendorLine vendorLine;
	private String category;
	private boolean needSave;
	private ConsumeRecord existRecord;

	public boolean isNeedSave() {
		return needSave;
	}

	public void setNeedSave(boolean needSave) {
		this.needSave = needSave;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public VendorLine getVendorLine() {
		return vendorLine;
	}

	public void setVendorLine(VendorLine vendorLine) {
		this.vendorLine = vendorLine;
	}
	public List<ConstantUtil.consumeErrorType> getError() {
		if (errors == null) {
			errors = new ArrayList<ConstantUtil.consumeErrorType>();
		}
		return errors;
	}

	public void setError(List<ConstantUtil.consumeErrorType> errors) {
		this.errors = errors;
	}

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ConsumeRecord getExistRecord() {
		return existRecord;
	}

	public void setExistRecord(ConsumeRecord existRecord) {
		this.existRecord = existRecord;
	}
}
