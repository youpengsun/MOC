package com.sap.moc.controller;


import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sap.moc.exception.WeChatException;
import com.sap.moc.service.INotificationService;
import com.sap.moc.service.IWeChatService;
import com.sap.moc.utils.ConstantReader;
import com.sap.moc.utils.aes.WXBizMsgCrypt;
import com.sap.moc.vo.PasNewsMessage;

@Controller
public class WeChatController {

	private static String token;
	private static String encodingAESKey;
	private static String corpId;
	private static WXBizMsgCrypt wxcpt;
	public static Properties properties = new Properties();

	static {
		try {
			// properties.load(WeChatController.class.getResourceAsStream("/properties/constants.properties"));
			// token = properties.getProperty("TOKEN");
			// encodingAESKey = properties.getProperty("ENCODINGAESKEY");
			// corpId = properties.getProperty("CORPID");
			token = ConstantReader.readSysParaByKey("TOKEN");
			encodingAESKey = ConstantReader.readSysParaByKey("ENCODINGAESKEY");
			corpId = ConstantReader.readSysParaByKey("CORPID");
			wxcpt = new WXBizMsgCrypt(token, encodingAESKey, corpId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired
	private INotificationService notificationService;

	@Autowired
	private IWeChatService wechatService;

	@RequestMapping("/wechat")
	public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String method = request.getMethod();
		String msg_signature = request.getParameter("msg_signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		PrintWriter out = response.getWriter();
		if (method.equals("GET")) {
			String echostr = request.getParameter("echostr");
			String result = "";
			if( msg_signature != null && timestamp != null && nonce != null && echostr != null){
				result = wxcpt.VerifyURL(msg_signature, timestamp, nonce, echostr);
			}
			out.print(result);
			
		} else if (method.equals("POST")) {
			InputStream inputStream = request.getInputStream();
			String postData = IOUtils.toString(inputStream, "UTF-8");
			postData = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, postData);
			String respMessage = wechatService.processRequest(postData, request);

			// Wechat request
			// if respMessage is null or is empty, write back a blank response
			if (respMessage == null || respMessage.length() == 0) {
				// 排重消息，直接回复success
				// Reference
				// http://mp.weixin.qq.com/wiki/1/6239b44c206cab9145b1d52c67e6c551.html
				out.print("success");

			} else {
				String encryptMsg = wxcpt.EncryptMsg(respMessage, timestamp, nonce);
				out.print(encryptMsg);

			}
			
			
		}
		out.close();
		
	}

	@ExceptionHandler(WeChatException.class)
	public void handleWeChatException(WeChatException exception) {
		PasNewsMessage msg = new PasNewsMessage(exception.getOpenID(), exception.getFromID(), "错误提醒",
				exception.getMessage());
		notificationService.sendPasNotification(msg);
	}

	@ExceptionHandler(Exception.class)
	public String handleException(Exception exception) {
		return "errorpage";
	}

}
