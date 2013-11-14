package org.arquillian.example.msg;

import javax.ejb.Local;

@Local
public interface MessageClient {

	void sendMessage();
}
