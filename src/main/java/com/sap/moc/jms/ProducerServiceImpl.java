package com.sap.moc.jms;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.JMSException;
import javax.jms.Session;
import com.sap.moc.vo.JmsMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.apache.activemq.ScheduledMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProducerServiceImpl implements IProducerService {
	private static final Logger logger = Logger.getLogger(ProducerServiceImpl.class);
	
    @Autowired
    JmsTemplate jmsTemplate;
	
	@Override
	public void sendMessage(final JmsMessage message) {
		try {
			jmsTemplate.send(new MessageCreator(){
				public Message createMessage(Session session) throws JMSException{
					
					long delay = 1000 * message.getTime();
					ObjectMessage obj = session.createObjectMessage(message);
					if(delay != 0){
						obj.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
					}
					return obj;
					
				}
			});
		
		} catch (Exception e) {
			logger.info("Send JMS exception: " + e.getMessage());
		}
		

	}

}
