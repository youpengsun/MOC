package com.sap.moc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sap.moc.service.IWeChatService;

@Controller
public class TestController {
    @Autowired
    private IWeChatService wechatService;
    
	@RequestMapping("/chat01")
	public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String xml = "<xml><ToUserName>CorpId</ToUserName><FromUserName>1001-2</FromUserName><CreateTime>123456789</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[CLICK]]></Event><EventKey><![CDATA[dailycount]]></EventKey><AgentID>1</AgentID></xml>";
 
		String respMessage = wechatService.processRequest(xml, request);
        System.out.println(respMessage.toString());

	}
	
	@RequestMapping("/testex")
	public void testException(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Exception ex = new Exception("the error happend! and cannot be recovvered!");
		
		throw ex;
	}
	
	@RequestMapping("/testrt")
	public void testRuntimeException(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
//		
//		int a = 0 / 0;
		
	}
}
