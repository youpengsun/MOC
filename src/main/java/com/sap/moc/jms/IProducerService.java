package com.sap.moc.jms;

import com.sap.moc.vo.JmsMessage;

public interface IProducerService {
	public void sendMessage(JmsMessage message);
}
