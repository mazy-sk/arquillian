package org.arquillian.example.msg;

import javax.annotation.Resource;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

@Decorator
public abstract class MessageRepositoryQueueDecorator implements MessageRepository {

	@Resource(mappedName="jms/ConnectionFactory")
	private ConnectionFactory cf;
	
	@Resource(mappedName="jms/JUnitQueue")
	private Destination destination;
	
	@Inject
	@Delegate
	@Any
	private MessageRepository messageRepository;

	@Override
	public void addMessage(String message) {
		messageRepository.addMessage(message);
		try {
			Connection connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(destination);
			Message response = session.createTextMessage("Message was save to repository.");
			messageProducer.send(response);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
