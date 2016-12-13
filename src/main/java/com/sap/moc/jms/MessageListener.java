package com.sap.moc.jms;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.moc.service.INotificationService;
import com.sap.moc.vo.JmsMessage;


public class MessageListener{	 
	@Autowired
	private IProducerService producerService;
	@Autowired
	private INotificationService notifyService;
	
	private static final Logger logger = Logger.getLogger(MessageListener.class);
	
	public void onMessage(JmsMessage message) {
		
        String errorCode = notifyService.sendNotificationFromJms(message);
        
        int time = message.getTime();
        if(!"0".equals(errorCode)){
        	
        	logger.error( message.getToUser() + " --- Send notification from JMS failed; Time: >>> " + time + " <<<" );
        	time += 3;
        	if(time < 15){
        		message.setTime(time);
            	producerService.sendMessage(message);
        	}
        	
        }
        else{
        	logger.info( message.getToUser() + " --- Send notification from JMS successfully; Time: >>> " + time + " <<<");
        }
//

	}

}
