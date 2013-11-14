package org.arquillian.example.msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

@Singleton
@Lock(LockType.READ)
public class SingletonMessageRepository implements MessageRepository {

	private List<String> messages;
	
	@Resource(mappedName="jms/ConnectionFactory")
	private ConnectionFactory cf;
	
	@Resource(mappedName="jms/JUnitQueue")
	private Destination destination;
			
	@Lock(LockType.WRITE)
	@Override
	public void addMessage(String message) {
		messages.add(message);
		
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

	@Override
	public List<String> getMessages() {
		return Collections.unmodifiableList(messages);
	}

	@Override
	public int getMessageCount() {
		return messages.size();
	}
	
	@PostConstruct
	void init() {
		messages = new ArrayList<String>();
	}

}
