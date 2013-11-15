package org.arquillian.example.msg;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

@Decorator
public abstract class MessageRepositoryThreadDecorator implements
		MessageRepository {

	
	@Inject
	@Delegate
	@Any
	private MessageRepository messageRepository;

	@Override
	public void addMessage(String message) {
		messageRepository.addMessage(message);
		synchronized(MessageClientTest.class) {
			MessageClientTest.class.notifyAll();
		}
	}
}
