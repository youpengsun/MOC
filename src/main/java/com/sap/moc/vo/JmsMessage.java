package com.sap.moc.vo;

import java.io.Serializable;

public class JmsMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7358734305335539247L;
	
	private String toUser;
	private String title;
	private String description;
	private String agentId;
	private int time;
	
	public JmsMessage( String toUser, String title, String description, String agentId){
		this.toUser = toUser;
		this.title = title;
		this.description = description;
		this.agentId = agentId;
	}
	
	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}



}
