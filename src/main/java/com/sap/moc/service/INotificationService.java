package com.sap.moc.service;

import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.vo.ConsumeSaveResult;
import com.sap.moc.vo.JmsMessage;
import com.sap.moc.vo.NewsMessage;
import com.sap.moc.vo.PasNewsMessage;

public interface INotificationService {
	public String sendPasNotification(PasNewsMessage msg);

	public String sendNotification(NewsMessage msg);
	
	public String sendSubscribeMsg(String touser);

	public PasNewsMessage buildNotificationToConsumer(String corpAccount, String consumeAccount,
			ConsumeSaveResult saveResult);
	
	public NewsMessage buildNotificationToUser(String consumeAccount, ConsumeSaveResult saveResult);

	public NewsMessage buildNotificationToVendor(String corpAccount, ConsumeRecord consumeRecord);

	public NewsMessage buildNewsMessageToAdmin(String jobId, Integer createTime, String status);

	public PasNewsMessage buildPasNotificationToVendor(String corpAccount, String vendorAccount, VendorLine line,
			int num, String url);
	
	public void sendJmsMessage(NewsMessage msg);
	
	public String sendNotificationFromJms(JmsMessage jmsMsg);

}
