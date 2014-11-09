package com.felix.interlogic.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import com.felix.interlogic.chat.swing.ChatWindow;

public class ClientThread extends Thread {
	private Socket server;
	private ChatWindow window;

	public ClientThread(Socket server, ChatWindow window) {
		this.server = server;
		this.window = window;
	}

	@Override
	public void run() {
		DataInputStream inp = null;
		try {
			inp = new DataInputStream(server.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				String line = inp.readUTF();
				window.write(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
