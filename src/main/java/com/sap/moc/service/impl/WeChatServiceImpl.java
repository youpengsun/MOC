package com.sap.moc.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.moc.dao.IVendorLineDAO;
import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.service.IConsumeService;
import com.sap.moc.service.INotificationService;
import com.sap.moc.service.IWeChatService;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.utils.IWxMessageDuplicateChecker;
import com.sap.moc.utils.MessageUtil;
import com.sap.moc.utils.TokenUtil;
import com.sap.moc.vo.ConsumeRecordVO;
import com.sap.moc.vo.ConsumeSaveResult;
import com.sap.moc.vo.NewsMessage;
import com.sap.moc.vo.PasNewsMessage;
import com.sap.moc.vo.QRCode;

/**
 * @author I074115
 *
 */
@Service
public class WeChatServiceImpl implements IWeChatService {
	@Autowired
	private IConsumeService consumeService;
	@Autowired
	private INotificationService notifyService;
	@Autowired
	private IVendorLineDAO lineDAO;
	@Autowired
	private IWxMessageDuplicateChecker duplicateChecker;

	private static final Logger logger = Logger.getLogger(WeChatServiceImpl.class);

	public String processRequest(String msg, HttpServletRequest request) {
		String respMessage = null;
		try {
			// Parse XML
			Map<String, Object> requestMap = MessageUtil.parseXml(msg);
			// Message Type
			String msgType = (String) requestMap.get("MsgType");

			/* Wechat Message Handling */
			switch (msgType) {
			// Text
			case MessageUtil.REQ_MESSAGE_TYPE_TEXT:
				break;
			// Image
			case MessageUtil.REQ_MESSAGE_TYPE_IMAGE:
				break;
			// Voice
			case MessageUtil.REQ_MESSAGE_TYPE_VOICE:
				break;
			// Video
			case MessageUtil.REQ_MESSAGE_TYPE_VIDEO:
				break;
			// Shortvideo
			case MessageUtil.REQ_MESSAGE_TYPE_SHORTVIDEO:
				break;
			// Location
			case MessageUtil.REQ_MESSAGE_TYPE_LOCATION:
				break;
			// Event Handling
			case MessageUtil.REQ_MESSAGE_TYPE_EVENT:
				// Event Type
				String eventType = (String) requestMap.get("Event");
				String eventKey = (String) requestMap.get("EventKey");
				switch (eventType) {
				//Subscription
				case MessageUtil.EVENT_TYPE_SUBSCRIBE:
					processSubscribe(requestMap);
					logger.info("subscribe");
					break;
				// Click Event
				case MessageUtil.EVENT_TYPE_CLICK:
					// Vendor daily count
					if (eventKey.equalsIgnoreCase(MessageUtil.EVENT_VENDOR_DAILY_COUNT)) {
						respMessage = processVendorDailyCount(requestMap, request);
					}
					logger.info("click daily count");
					break;
				// Scancode Event
				case MessageUtil.EVENT_TYPE_SCANCODE_WAIT:
					// Consume Meal
					if (isDuplicateMessage(requestMap)) {
						logger.info("duplicated message");
						return null;
					}
					if (eventKey.equalsIgnoreCase(MessageUtil.EVENT_SCANCODE_CONSUME_MEAL)) {
						respMessage = processEventScanMeal(requestMap);
					}
					break;
				// batch upload job
				case MessageUtil.EVENT_TYPE_BATCH_JOB_RESULT:
					processBatchUploadResult(requestMap);
					break;
				}
				break;
			}
		} catch (Exception e) {
			logger.error("处理请求异常: "+e.getMessage());
			return null;
		}
		return respMessage;
	}

	protected boolean isDuplicateMessage(Map<String, Object> requestMap) {

		String messageId = "";
		// String.valueOf(requestMap.get("msgId"));
		String createTime = String.valueOf(requestMap.get("CreateTime"));
		String fromUserName = String.valueOf(requestMap.get("FromUserName"));
		if (requestMap.get("msgId") == null) {
			// messageId = String.valueOf(wxMessage.getCreateTime())
			// + "-" +String.valueOf(wxMessage.getAgentId() == null ? "" :
			// wxMessage.getAgentId())
			// + "-" + wxMessage.getFromUserName()
			// + "-" + String.valueOf(wxMessage.getEventKey() == null ? "" :
			// wxMessage.getEventKey())
			// + "-" + String.valueOf(wxMessage.getEvent() == null ? "" :
			// wxMessage.getEvent())
			// ;
			messageId = createTime + "-" + fromUserName;
		} else {
			messageId = String.valueOf(requestMap.get("msgId"));
		}

		if (duplicateChecker.isDuplicate(messageId)) {
			return true;
		}
		return false;

	}
	
	private String processSubscribe(Map<String, Object> mes){
		String user = (String) mes.get("FromUserName");
		String respMessage = notifyService.sendSubscribeMsg(user);
		return respMessage;
	}

	@SuppressWarnings("unchecked")
	private String processEventScanMeal(Map<String, Object> mes) {
		String respMessage = null;

		String consumeAccount = (String) mes.get("FromUserName");
		String corpAccount = (String) mes.get("ToUserName");

		Map<String, Object> map_codeinfo = (Map<String, Object>) mes.get("ScanCodeInfo");
		String scanCode = (String) map_codeinfo.get("ScanResult");

		ConsumeSaveResult saveResult = consumeService.saveConsumeRecord(consumeAccount, scanCode);
		ConsumeRecord newRecord = saveResult.getNewRecord();
		String consumeStatus = newRecord.getStatus();

		// Notify User (Success/Fail)
		PasNewsMessage message = notifyService.buildNotificationToConsumer(corpAccount, consumeAccount, saveResult);
		respMessage = notifyService.sendPasNotification(message);
		
		// begin--- change the passive message to initiative
		respMessage = "";
		NewsMessage messageToUser = notifyService.buildNotificationToUser(consumeAccount, saveResult);
		sendNotification(messageToUser);
		//end----

		// Notify Vendor (Success)
		if (consumeStatus == ConstantUtil.CONSUME_STATUS_SUCCESS) {

			NewsMessage newsMessage = notifyService.buildNotificationToVendor(corpAccount, newRecord);
			sendNotification(newsMessage);
			/*
			String errorCode = notifyService.sendNotification(newsMessage);
			if ("-1".equals(errorCode)) {
				//TODO SMS notification
				logger.error("Send msg to vendor failed, turn to JMS; error code:" + errorCode);
				notifyService.sendJmsMessage(newsMessage);
				
			}
			else if("40001".equals(errorCode)){
				logger.error("Send msg to vendor failed, turn to JMS; error code:" + errorCode);
				TokenUtil.updateAccessToken();
				notifyService.sendJmsMessage(newsMessage);
			}
			else if(ConstantUtil.NET_EXCEPTION.equals(errorCode)){
				logger.error("Send msg to vendor failed, turn to JMS; error code:" + errorCode);
				notifyService.sendJmsMessage(newsMessage);
			}
			else if(!"0".equals(errorCode)){
				logger.error("Send notification to vendor fail, error code:" + errorCode);
			}*/
		}
		return respMessage;
	}
	
	private void sendNotification(NewsMessage message){
		String errorCode = notifyService.sendNotification(message);
		if ("-1".equals(errorCode)) {
			logger.error("Send msg to > " + message.getTouser() + " < failed, turn to JMS; error code:" + errorCode);
			notifyService.sendJmsMessage(message);
			
		}
		else if("40001".equals(errorCode)){
			logger.error("Send msg to > " + message.getTouser() + " < failed, turn to JMS; error code:" + errorCode);
			TokenUtil.updateAccessToken();
			notifyService.sendJmsMessage(message);
		}
		else if(ConstantUtil.NET_EXCEPTION.equals(errorCode)){
			logger.error("Send msg to > " + message.getTouser() + " < failed, turn to JMS; error code:" + errorCode);
			notifyService.sendJmsMessage(message);
		}
		else if(!"0".equals(errorCode)){
			logger.error("Send msg to > " + message.getTouser() + " < failed; error code:" + errorCode);
		}
	}
	
	private String processBatchUploadResult(Map<String, Object> mes) {
		@SuppressWarnings("unchecked")
		Map<String, Object> batchJob = (Map<String, Object>) mes.get("BatchJob");

		NewsMessage newsMessage = notifyService.buildNewsMessageToAdmin(batchJob.get("JobId").toString(),
				 Integer.parseInt(mes.get("CreateTime").toString())*1000 , batchJob.get("ErrMsg").toString());
		return notifyService.sendNotification(newsMessage);

	}

	// Process Daily Count Event for Vendor
	private String processVendorDailyCount(Map<String, Object> mes, HttpServletRequest request) {

		int consumeCount = 0;

		String vendorAccount = mes.get("FromUserName").toString();
		String corpAccount = mes.get("ToUserName").toString();

		QRCode code = new QRCode(vendorAccount); // Same as xxxxx-xx
		VendorLine line = lineDAO.getLinebyQRCode(code);

		// build time range of today
		Calendar cal = CommonUtil.getCurrentCalendar();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);		
		int day = cal.get(Calendar.DATE);
		cal.set(year, month, day, 0, 0, 0);
		Timestamp from = new Timestamp(cal.getTimeInMillis());
		cal.set(year, month, day, 23, 59, 59);
		Timestamp to = new Timestamp(cal.getTimeInMillis());

		// query consume records
		List<ConsumeRecordVO> records = consumeService.getRecordsByVendorPeriod(line, ConstantUtil.CONSUME_STATUS_SUCCESS,
				from, to);
		consumeCount = records.size();
		// build URL
		String requestedURL = request.getRequestURL().toString();
		String url = requestedURL.replaceAll(request.getServletPath(), "/mobile/vendor/dailycount.html" + "?t="+Calendar.getInstance().getTimeInMillis());
		
		// build message back		
		PasNewsMessage message = notifyService.buildPasNotificationToVendor(corpAccount, vendorAccount, line,
				consumeCount, url);
		
		return notifyService.sendPasNotification(message);
	}

}