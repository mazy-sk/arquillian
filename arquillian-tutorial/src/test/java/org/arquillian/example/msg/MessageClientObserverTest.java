package org.arquillian.example.msg;

import java.io.File;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MessageClientObserverTest {

	@Inject
	private MessageClient messageClient;
	@Inject
	private MessageRepository messageRepository;
		
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap
				.create(JavaArchive.class, "messageObserverTest.jar")
				.addClasses(MessageClient.class, MessageClientBean.class,
						MessageConsumer.class, MessageRepository.class,
						SingletonMessageRepository.class,
						MessageRepositoryObserverDecorator.class, TestEvent.class)
				.addAsManifestResource(new File("src/test/resources/META-INF/MessageClientObserverTest-beans.xml"),"beans.xml");
		System.out.println(jar.toString(true));
		return jar;
	}
	
	public void receiveMessage(@Observes TestEvent event) {
		synchronized (MessageClientObserverTest.class) {
			MessageClientObserverTest.class.notifyAll();
		}
	}

	@Test
	public void testSendMessage() throws InterruptedException {
		messageClient.sendMessage();
		synchronized (MessageClientObserverTest.class) {
			long start = System.currentTimeMillis();
			MessageClientObserverTest.class.wait(8000);
			System.out.println("Wait time:" + (System.currentTimeMillis() - start));
		}		
		Assert.assertEquals(1, messageRepository.getMessageCount());
	}

}
