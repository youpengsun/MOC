package com.sap.moc.vo;

import java.util.Map;

public class WechatBatchSyncUser {
    private String media_id;
    private Map<String, String> callback;
    
	public String getMedia_id() {
		return media_id;
	}
	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}
	
	public Map<String, String> getCallback() {
		return callback;
	}
	
	public void setCallback(Map<String, String> callback) {
		this.callback = callback;
	}
    
}
