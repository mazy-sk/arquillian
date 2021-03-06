package org.arquillian.example.msg;

import java.io.File;

import javax.ejb.EJB;
import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MessageClientTest {

	@EJB
	private MessageClient messageClient;
	@Inject
	private MessageRepository messageRepository;
		
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap
				.create(JavaArchive.class, "messageThreadTest.jar")
				.addClasses(MessageClient.class, MessageClientBean.class,
						MessageConsumer.class, MessageRepository.class,
						SingletonMessageRepository.class, MessageRepositoryThreadDecorator.class)
				.addAsManifestResource(new File("src/test/resources/META-INF/MessageClientThreadTest-beans.xml"),"beans.xml");
		System.out.println(jar.toString(true));
		return jar;
	}

	@Test
	public void testSendMessage() throws InterruptedException {
		messageClient.sendMessage();
		synchronized (MessageClientTest.class) {
			long start = System.currentTimeMillis();
			MessageClientTest.class.wait(4000);
			System.out.println("Wait time:" + (System.currentTimeMillis() - start));
		}
		
		Assert.assertEquals(1, messageRepository.getMessageCount());
	}

}
