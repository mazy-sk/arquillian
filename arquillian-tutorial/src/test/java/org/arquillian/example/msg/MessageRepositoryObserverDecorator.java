package org.arquillian.example.msg;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

@Decorator
public abstract class MessageRepositoryObserverDecorator implements
		MessageRepository {

	@Inject
	private Event<TestEvent> event;
	
	@Inject
	@Delegate
	@Any
	private MessageRepository messageRepository;

	@Override
	public void addMessage(String message) {
		messageRepository.addMessage(message);
		event.fire(new TestEvent());
	}
}
