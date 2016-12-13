package com.sap.moc.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.test.BaseJunit4Test;
import com.sap.moc.vo.ConsumeSaveResult;
import com.sap.moc.vo.PasNewsMessage;

public class WeChatServiceTest extends BaseJunit4Test {

	@Autowired
	private IWeChatService service;
	
	@Autowired
	private IConsumeService consumeService;
	
	@Autowired
	private INotificationService notifyService;

	@Test
	public void testProcessRequest() {
		// fail("Not yet implemented");
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><ToUserName>toUser555</ToUserName><FromUserName>FromUser555</FromUserName><CreateTime>1408090606</CreateTime><MsgType>event</MsgType><Event>scancode_waitmsg</Event><EventKey>consume</EventKey><ScanCodeInfo><ScanType>qrcode</ScanType><ScanResult>00000001900000001</ScanResult></ScanCodeInfo><AgentID>1555</AgentID></xml>";
		// String result = service.processRequest(msg);
		// System.out.println(result);
	}

	@Test
	public void testNotification() {
		String employeeId = "I065892";
		String scanCode = "10002-1";
		String consumeAccount = "";
		ConsumeSaveResult saveResult = consumeService.saveConsumeRecord(employeeId, scanCode);
		
		ConsumeRecord upd_record = saveResult.getNewRecord();
		String consumeStatus = upd_record.getStatus();

		// Notify User (Success/Fail)
		PasNewsMessage message = notifyService.buildNotificationToConsumer(employeeId, consumeAccount, saveResult);
		
		System.out.println(message.getArticles().get(0).getTitle());
		System.out.println(message.getArticles().get(0).getDescription());
	}

}
