package com.felix.socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MessageOutputStream extends ObjectOutputStream {

	public MessageOutputStream(OutputStream outputStream) throws IOException {
		super(outputStream);
	}

	public void write(Message message) {
		try {
			writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
