package com.sap.moc.vo;

import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.utils.ConstantUtil;

public class ConsumeSaveResult {
	private ConsumeRecord newRecord;
	
	private ConsumeRecord existRecord;
	
	private ConstantUtil.consumeErrorType error;

	public ConstantUtil.consumeErrorType getError() {
		return error;
	}

	public void setError(ConstantUtil.consumeErrorType error) {
		this.error = error;
	}


	public ConsumeRecord getExistRecord() {
		return existRecord;
	}

	public void setExistRecord(ConsumeRecord existRecord) {
		this.existRecord = existRecord;
	}

	public ConsumeRecord getNewRecord() {
		return newRecord;
	}

	public void setNewRecord(ConsumeRecord newRecord) {
		this.newRecord = newRecord;
	} 
}
