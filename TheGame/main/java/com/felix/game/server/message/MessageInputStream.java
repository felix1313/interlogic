package com.felix.game.server.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;


public class MessageInputStream extends ObjectInputStream {

	public MessageInputStream(InputStream inputStream) throws IOException {
		super(inputStream);
	}

	public Message readMessage() throws ClassCastException{
		Message result = null;
		try {
			result = (Message) readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	
	
	public String readString() throws ClassCastException{
		String result = null;
		try {
			result = (String) readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
