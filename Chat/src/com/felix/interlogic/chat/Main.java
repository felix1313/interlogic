package com.felix.interlogic.chat;

import java.io.IOException;

import com.felix.interlogic.socket.Server;

public class Main {

	public static void main(String[] args) {
		new Server().start();
		try {
			ChatClient client = new ChatClient();
			// ChatClient client1= new ChatClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
