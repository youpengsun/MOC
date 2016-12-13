package com.sap.moc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.entity.Employee;
import com.sap.moc.entity.Vendor;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.jms.IProducerService;
import com.sap.moc.service.IConsumeService;
import com.sap.moc.service.INotificationService;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.utils.ConstantReader;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.utils.I18nUtil;
import com.sap.moc.utils.MessageUtil;
import com.sap.moc.utils.QRCodeUtil;
import com.sap.moc.utils.TokenUtil;
import com.sap.moc.vo.Article;
import com.sap.moc.vo.ConsumeSaveResult;
import com.sap.moc.vo.JmsMessage;
import com.sap.moc.vo.NewsMessage;
import com.sap.moc.vo.PasNewsMessage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class NotificationServiceImpl implements INotificationService {
	private static final Logger logger = Logger.getLogger(NotificationServiceImpl.class);

	@Autowired
	private IConsumeService consumeService;
	
	@Autowired
	private IProducerService producerService;

	@Override
	public String sendPasNotification(PasNewsMessage msg) {
		// return the XML string
		return MessageUtil.textMessageToXml(msg);
	}

	@Override
	public String sendNotification(NewsMessage newsMessage) {
		JSONObject message = JSONObject.fromObject(newsMessage);

		return sendMsg(message.toString());
	}

	@Override
	public String sendSubscribeMsg(String touser) {
		String respMessage = "";
		String mediaId = getSubscribeMediaId();
		if(mediaId != null && (!"".equals(mediaId))){
			Map<String, String> mpnews = new HashMap<String, String>();
			//String mediaId = ConstantReader.readSysParaByKey("SUBSCRIBE_MEDIA_ID");
			mpnews.put("media_id", mediaId);
			JSONObject object = new JSONObject();
			object.put("touser", touser);
			object.put("msgtype", "mpnews");
			object.put("agentid", ConstantReader.readByKey("CANTEEN_AGENTID"));
			object.put("mpnews", mpnews.toString());
			respMessage = object.toString();
			sendMsg(respMessage);
		}

		return respMessage;
	}

	@Override
	public PasNewsMessage buildNotificationToConsumer(String corpAccount, String consumerAccount,
			ConsumeSaveResult saveResult) {

		
		ConsumeRecord consumeRecord = saveResult.getNewRecord();

		// Build Name format base on language setting
		Locale locale = Locale.CHINESE;
		String nameLocale = consumerAccount;
		Employee employee = consumeRecord.getEmployee();
		if (employee != null) {
			locale = CommonUtil.formatEmployeeName(employee);
			nameLocale = employee.getFormattedName();
		}
		String consumeStatus = consumeRecord.getStatus();

		// build contents by locale
		String messageTitle = "";
		String messageContent = "";
		if (consumeStatus == ConstantUtil.CONSUME_STATUS_SUCCESS) {

			messageTitle = I18nUtil.getKey("CONSUME_SUCCESS_TITLE", locale);
			messageContent = I18nUtil.getKey("CONSUME_SUCCESS_CONTENT", locale);
			messageContent = MessageFormat.format(messageContent, nameLocale);
			messageContent = messageContent + "\n\n" + buildConsumeMessageBlock(consumeRecord, locale);

		} else {
			messageTitle = I18nUtil.getKey("CONSUME_FAILED_TITLE", locale);
			messageContent = I18nUtil.getKey("CONSUME_FAILED_CONTENT", locale);

			
			String errorContent;
			try {
				errorContent = I18nUtil.getKey(consumeRecord.getComment(), locale);

				if (errorContent == null && "".equals(errorContent)) {
					errorContent = consumeRecord.getComment();
				}
			} catch (Exception e) {
				errorContent = consumeRecord.getComment();
			}
			
			
			messageContent = MessageFormat.format(messageContent, errorContent);

			if (saveResult.getError() == ConstantUtil.consumeErrorType.ALREADY_CONSUMED) {
				ConsumeRecord existRecord = saveResult.getExistRecord();
				if (existRecord != null) {
					String messageContent_extra = buildConsumeMessageBlock(existRecord, locale);
					messageContent = messageContent + "\n\n" + messageContent_extra;
				}
			} else if (saveResult.getError() == ConstantUtil.consumeErrorType.NOT_AVAILABLE_TIME) {

				String messageContent_time = buildMealTimeMessageBlock(consumeRecord.getVendorLine().getVendor(),
						locale);
				messageContent = messageContent + "\n\n" + messageContent_time;
			}
		}

		PasNewsMessage message = new PasNewsMessage(consumerAccount, corpAccount, messageTitle, messageContent);
		return message;
	}

	private String buildConsumeMessageBlock(ConsumeRecord record, Locale locale) {

		String messageContent = I18nUtil.getKey("CONSUME_TRANSACTION_INFO", locale);

		String vendorName = record.getVendorLine().getVendor().getName();
		String lineName = record.getVendorLine().getName();
		String trancode = record.getTransactionCode();
		String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(record.getTime());
		Object[] placeStrDescription = { vendorName, lineName, time, trancode };
		messageContent = MessageFormat.format(messageContent, placeStrDescription);

		return messageContent;
	}

	private String buildMealTimeMessageBlock(Vendor vendor, Locale locale) {
		String messageContent = "";
		String lunchBegin = ConstantReader.readTimeByKey("LUNCH_BEGIN").substring(0, 5);
		String lunchEnd = ConstantReader.readTimeByKey("LUNCH_END").substring(0, 5);

		String messageContent_Lunch = I18nUtil.getKey("LUNCN_TIME_INFO", locale);
		messageContent_Lunch = MessageFormat.format(messageContent_Lunch, lunchBegin, lunchEnd);
		messageContent = messageContent_Lunch;
		if (vendor.getVendorType().equals(ConstantUtil.VENDOR_TYPE_INTERNAL.toString())) {
			String dinnerBegin = ConstantReader.readTimeByKey("DINNER_BEGIN").substring(0, 5);
			String dinnerEnd = ConstantReader.readTimeByKey("DINNER_END").substring(0, 5);

			String messageContent_Dinner = I18nUtil.getKey("DINNER_TIME_INFO", locale);
			messageContent_Dinner = MessageFormat.format(messageContent_Dinner, dinnerBegin, dinnerEnd);
			messageContent = messageContent + "\n" + messageContent_Dinner;
		}
		return messageContent;
	}
	
	@Override
	public NewsMessage buildNotificationToUser(String consumerAccount, ConsumeSaveResult saveResult) {
		
		ConsumeRecord consumeRecord = saveResult.getNewRecord();

		// Build Name format base on language setting
		Locale locale = Locale.CHINESE;
		String nameLocale = consumerAccount;

		Employee employee = consumeRecord.getEmployee();
		if (employee != null) {
			locale = CommonUtil.formatEmployeeName(employee);
			nameLocale = employee.getFormattedName();
		}
		String consumeStatus = consumeRecord.getStatus();

		// build contents by locale
		String messageTitle = "";
		String messageContent = "";
		if (consumeStatus == ConstantUtil.CONSUME_STATUS_SUCCESS) {

			messageTitle = I18nUtil.getKey("CONSUME_SUCCESS_TITLE", locale);
			messageContent = I18nUtil.getKey("CONSUME_SUCCESS_CONTENT", locale);
			messageContent = MessageFormat.format(messageContent, nameLocale);
			messageContent = messageContent + "\n\n" + buildConsumeMessageBlock(consumeRecord, locale);

		} else {
			messageTitle = I18nUtil.getKey("CONSUME_FAILED_TITLE", locale);
			messageContent = I18nUtil.getKey("CONSUME_FAILED_CONTENT", locale);

			String errorContent;
			try {
				errorContent = I18nUtil.getKey(consumeRecord.getComment(), locale);

				if (errorContent == null && "".equals(errorContent)) {
					errorContent = consumeRecord.getComment();
				}
			} catch (Exception e) {
				errorContent = consumeRecord.getComment();
			}

			messageContent = MessageFormat.format(messageContent, errorContent);

			if (saveResult.getError() == ConstantUtil.consumeErrorType.ALREADY_CONSUMED) {
				ConsumeRecord existRecord = saveResult.getExistRecord();
				if (existRecord != null) {
					String messageContent_extra = buildConsumeMessageBlock(existRecord, locale);
					messageContent = messageContent + "\n\n" + messageContent_extra;
				}
			} else if (saveResult.getError() == ConstantUtil.consumeErrorType.NOT_AVAILABLE_TIME) {

				String messageContent_time = buildMealTimeMessageBlock(consumeRecord.getVendorLine().getVendor(),
						locale);
				messageContent = messageContent + "\n\n" + messageContent_time;
			}
		}

		String agentId = ConstantReader.readByKey("CANTEEN_AGENTID");
		
		NewsMessage message = new NewsMessage(consumerAccount, messageTitle, messageContent, agentId);

		return message;
	}
	
	@Override
	public NewsMessage buildNotificationToVendor(String corpAccount, ConsumeRecord consumeRecord) {
		// Send message to vendor
		String vendorAccount = QRCodeUtil.buildVenlineOpenId(consumeRecord.getVendorLine());
		String vendorName = consumeRecord.getVendorLine().getVendor().getName();
		String lineName = consumeRecord.getVendorLine().getName();
		String createTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(consumeRecord.getTime());
		String trancode = consumeRecord.getTransactionCode();

		String messageTitle = I18nUtil.getKey("CONSUME_SUCCESS_TITLE_VENDOR", Locale.CHINESE);
		String messageContent = I18nUtil.getKey("CONSUME_SUCCESS_CONTENT_VENDOR", Locale.CHINESE);

		// placeholder of title
		int todayCount = consumeService.getVendorLineTodayActiveCount(consumeRecord.getVendorLine().getId());
		Object[] placeStrTitle = { todayCount };
		String finalTitle = MessageFormat.format(messageTitle, placeStrTitle);

		// placeholder of Description
		Object[] placeStrDescription = { vendorName, lineName, createTime, trancode };
		String finalDescription = MessageFormat.format(messageContent, placeStrDescription);

		String agentId = ConstantReader.readByKey("VENDOR_AGENTID");
		NewsMessage message = new NewsMessage(vendorAccount, finalTitle, finalDescription, agentId);
		return message;
	}

	// Send message to vendor
	private String sendMsg(String msgContent) {

		String errorcode = "";

		// Get access token
		String access_token = TokenUtil.getAccessToken();
		String url = ConstantReader.readSysParaByKey("CREATE_SESSION_URL") + access_token;
		try {

			HttpsURLConnection connection = buildConnection(url);

			// write to output stream
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(msgContent.getBytes("UTF-8"));

			// Input Stream
			InputStream inputStream = connection.getInputStream();
			String input = IOUtils.toString(inputStream);

			// get the returned result
			JSONObject result = JSONObject.fromObject(input);
			errorcode = result.get("errcode").toString();

		} catch (Exception e) {
			logger.error("Send Message Error. " + "Exception:" + e.getMessage());
			errorcode = ConstantUtil.NET_EXCEPTION;
			return errorcode;
		}

		return errorcode;
	}

	private HttpsURLConnection buildConnection(String urlString) {

		HttpsURLConnection connection = null;
		try {
			URL url = new URL(urlString);
			connection = (HttpsURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-type", "application/json;charset=UTF-8");
			connection.setDoOutput(true);
		} catch (Exception e) {
			logger.error("Connection Error. " + "Exception:" + e.getMessage());
			//e.printStackTrace();
		}

		return connection;
	}

	private String getSubscribeMediaId() {

		String accessToken = TokenUtil.getAccessToken();
		String mediaUrl = ConstantReader.readByKey("GET_MATERIAL_MEDIA_URL");
		mediaUrl = mediaUrl.replaceAll("&1", accessToken);

		HttpsURLConnection connection = buildConnection(mediaUrl);
		JSONObject result = null;
		String mediaId = "";

		JSONObject reqContent = new JSONObject();
		reqContent.put("type", "mpnews");
		reqContent.put("agentid", ConstantReader.readByKey("CANTEEN_AGENTID"));
		reqContent.put("offset", "0");
		reqContent.put("count", "10");

		try {
			// write to output stream

			OutputStream outputStream = connection.getOutputStream();
			String req = reqContent.toString();
			outputStream.write(req.getBytes("UTF-8"));

			// Input Stream
			InputStream inputStream = connection.getInputStream();
			String input = IOUtils.toString(inputStream);

			// get the returned result
			result = JSONObject.fromObject(input);

		} catch (IOException e) {
			logger.error("IO Error. " + "Exception:" + e.getMessage());
		}

		String errorCode = result.get("errcode").toString();

		if ("0".equals(errorCode)) {
			try {
				JSONArray itemList = (JSONArray) result.get("itemlist");
				for (int i = 0; i < itemList.size(); i++) {
					JSONObject item = (JSONObject) itemList.get(i);
					JSONObject content = (JSONObject) item.get("content");
					JSONArray articles = (JSONArray) content.get("articles");
					JSONObject article = (JSONObject) articles.get(0);  // the subscribe material should have only one article
					String title  = (String) article.get("title");
					if( title.contains("Welcome")){
						mediaId = item.getString("media_id");
						break;
					}
				}
			} catch (Exception e) {
				logger.error("Get Material error. " + "Exception:" + e.getMessage());
			}
		}


		return mediaId;
	}

	@Override
	public NewsMessage buildNewsMessageToAdmin(String jobId, Integer createTime, String status) {
		String messageContent = I18nUtil.getKey("WechatBatchSyncUserMessageContent", Locale.ENGLISH);
		Object[] placeStrDescription = { jobId, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(createTime * 1000),
				status };
		String description = MessageFormat.format(messageContent, placeStrDescription);
		return new NewsMessage(ConstantReader.readByKey("ADMIN_ID"),
				I18nUtil.getKey("WechatBatchJobMessageTitle", Locale.ENGLISH), description,
				ConstantReader.readByKey("CANTEEN_AGENTID"));

	}

	@Override
	public PasNewsMessage buildPasNotificationToVendor(String corpAccount, String vendorAccount, VendorLine line,
			int num, String url) {

		String title = I18nUtil.getKey("DAILY_CONSUME_TITLE", Locale.CHINESE);
		String vendor_name = line.getVendor().getName();
		String line_name = line.getName();

		String messageContent = I18nUtil.getKey("DAILY_CONSUME_CONTENT", Locale.CHINESE);
		Object[] placeStrDescription = { vendor_name, line_name, num };
		String description = MessageFormat.format(messageContent, placeStrDescription);

		PasNewsMessage message = new PasNewsMessage(vendorAccount, corpAccount, title, description, url);
		return message;
	}

	@Override
	public void sendJmsMessage(NewsMessage msg) {
		JmsMessage jmsMessage = convertNewsToJms(msg);
		producerService.sendMessage(jmsMessage);

	}
	
	@Override
	public String sendNotificationFromJms(JmsMessage jmsMsg) {
		NewsMessage news = convertJmsToNews(jmsMsg);
		String errorCode = sendNotification(news);
		return errorCode;
	}
	
	private JmsMessage convertNewsToJms(NewsMessage msg){
		if(msg != null && msg.getNews() != null){
			Article article = msg.getNews().getArticles().get(0);
			if(article != null){
				JmsMessage jmsMsg = new JmsMessage(msg.getTouser(), article.getTitle(), article.getDescription(), msg.getAgentid());
				return jmsMsg;
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}	
	}

	private NewsMessage convertJmsToNews(JmsMessage jmsMsg){
		if(jmsMsg != null){
			NewsMessage news = new NewsMessage(jmsMsg.getToUser(), jmsMsg.getTitle(), jmsMsg.getDescription(), jmsMsg.getAgentId());
			return news;
		}
		else{
			return null;
		}
			
	}
	


}