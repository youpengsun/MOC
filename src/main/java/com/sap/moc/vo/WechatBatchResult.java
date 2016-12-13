package com.sap.moc.vo;

import java.util.ArrayList;

/**
 * For wechat upload result
 * @author I074193
 *
 */
public class WechatBatchResult {
	private String errcode;
	private String errmsg;
	private String status;
	private String type;
	private String total;
	private String percentage;
	private String remain_time;		//the field name is different from Wechat official help document
	private ArrayList<WechatBatchResultUser> result;
	
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	public String getRemain_time() {
		return remain_time;
	}
	public void setRemain_time(String remain_time) {
		this.remain_time = remain_time;
	}
	public ArrayList<WechatBatchResultUser> getResult() {
		return result;
	}
	public void setResult(ArrayList<WechatBatchResultUser> result) {
		this.result = result;
	}

	
}
