package org.arquillian.example.msg;

import java.util.List;

import javax.ejb.Local;

@Local
public interface MessageRepository {

	void addMessage(String message);
    List<String> getMessages();
    int getMessageCount();
}
