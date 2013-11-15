package org.arquillian.example.msg;

import java.io.File;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MessageClientQueueTest {

	@Inject
	private MessageClient messageClient;
	@Inject
	private MessageRepository messageRepository;
	
	@Resource(mappedName="jms/ConnectionFactory")
	private ConnectionFactory cf;
	
	@Resource(mappedName="jms/JUnitQueue")
	private Destination destination;
	
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap
				.create(JavaArchive.class, "messageQueueTest.jar")
				.addClasses(MessageClient.class, MessageClientBean.class,
						MessageConsumer.class, MessageRepository.class,
						SingletonMessageRepository.class, MessageRepositoryQueueDecorator.class,
						MessageRepositoryObserverDecorator.class)
				.addAsManifestResource(new File("src/test/resources/META-INF/MessageClientQueueTest-beans.xml"), "beans.xml");
		System.out.println(jar.toString(true));
		return jar;
	}

	@Test
	public void testSendMessage() throws InterruptedException {
		messageClient.sendMessage();

		Connection connection = null;
		try {
			connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			javax.jms.MessageConsumer messageConsumer = session.createConsumer(destination);
			connection.start();
			Message message = messageConsumer.receive(4000);
			Assert.assertNotNull(message);
			TextMessage textMessage = (TextMessage) message;
			Assert.assertEquals("Message was save to repository.", textMessage.getText());
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
		
		Assert.assertEquals(1, messageRepository.getMessageCount());
	}

}
