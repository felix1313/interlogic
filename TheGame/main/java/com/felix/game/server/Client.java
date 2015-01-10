package com.felix.game.server;

import java.io.IOException;
import java.net.Socket;

import com.felix.socket.Message;
import com.felix.socket.MessageOutputStream;

public class Client {
	private Socket socket;
	private MessageOutputStream outputStream;

	public Client(Socket socket) {
		this.socket = socket;
		try {
			this.outputStream = new MessageOutputStream(
					socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	public void write(Message m) {
		this.outputStream.write(m);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
