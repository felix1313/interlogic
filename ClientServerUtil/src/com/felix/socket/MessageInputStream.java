package com.felix.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class MessageInputStream extends ObjectInputStream {

	public MessageInputStream(InputStream inputStream) throws IOException {
		super(inputStream);
	}

	public Message readMessage() {
		Message result = null;
		try {
			result = (Message) readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
