package org.arquillian.example.msg;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

@Stateless
public class MessageClientBean implements MessageClient {

	@Resource(mappedName="jms/ConnectionFactory")
	private ConnectionFactory cf;
	
	@Resource(mappedName="jms/Queue")
	private Destination destination;
	
	@Override
	public void sendMessage() {
		try {
			Connection connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(destination);
			Message message = session.createTextMessage("Hello, World");
			messageProducer.send(message);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
