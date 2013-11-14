package org.arquillian.example.msg;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(mappedName="jms/Queue", 
	activationConfig={@ActivationConfigProperty(propertyName="acknowledgeMode",
												propertyValue="Auto-acknowledge"),
					  @ActivationConfigProperty(propertyName="destinationType",
												propertyValue="javax.jms.Queue")})
public class MessageConsumer implements MessageListener {

	@Resource
	private MessageDrivenContext mdc;
	
	@EJB
	private MessageRepository messageRepo;
	
	@Override
	public void onMessage(Message message) {
		TextMessage msg = null;
		if (message instanceof TextMessage) {
			msg = (TextMessage) message;
			try {
				String text = msg.getText();
				messageRepo.addMessage(text);
				System.out.println("Received message: "+ text);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				mdc.setRollbackOnly();
				e.printStackTrace();
			}
		} else {
			System.out.println("Received message of wrong type: "+ message.getClass());
		}
	}

}
