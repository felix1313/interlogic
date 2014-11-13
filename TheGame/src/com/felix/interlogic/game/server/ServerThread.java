package com.felix.interlogic.game.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {
	private Server server;
	private Socket clientSocket;

	public ServerThread(Server server, Socket clientSocket) {
		super();
		this.server = server;
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			DataInputStream in = new DataInputStream( clientSocket
					.getInputStream());

			while (true) {
				String message = in.readUTF();

				server.sendAll(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
