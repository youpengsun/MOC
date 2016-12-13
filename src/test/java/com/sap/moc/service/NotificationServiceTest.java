package com.sap.moc.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.moc.test.BaseJunit4Test;

public class NotificationServiceTest extends BaseJunit4Test {

	@Autowired
	private INotificationService service;

	@Test
	public void testSendNotificationToUser() {
		String touser = "I074184";
		System.getProperties().setProperty("https.proxyHost", "proxy");
		System.getProperties().setProperty("https.proxyPort", "8080");
		for(int i = 0 ; i< 10; i++){
			String resp = service.sendSubscribeMsg(touser);
			System.out.println(resp);
		}
		
		

	}

//	@Test
//	public void testSendNotficationToVendor() throws InterruptedException, IOException {
//		String DevMode = ConstantReader.readSysParaByKey("DEV_FLAG");
//		if (DevMode.equals("1")) {
//			System.getProperties().setProperty("https.proxyHost", "proxy");
//			System.getProperties().setProperty("https.proxyPort", "8080");
//		}
//
//		NewsMessage msg = new NewsMessage("@all", "title", "description", "");
//		String result = service.sendNotfication(msg);
//		System.out.println(result);
//	}

}
