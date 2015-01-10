package com.felix.game.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.felix.socket.Message;
import com.felix.socket.MessageInputStream;

public class ServerThread extends Thread {
	private Server server;
	private Client client;
	private GameRoom gameRoom;

	public ServerThread(Server server, Client client) {
		super();
		this.server = server;
		this.client = client;
	}

	@Override
	public void run() {
		MessageInputStream in = null;

		try {
			in = new MessageInputStream(client.getSocket().getInputStream());
			boolean createNewRoom = in.readBoolean();

			if (createNewRoom == true) {
				String password = in.readUTF();
				this.gameRoom = new GameRoom(client, password);
				server.addGame(gameRoom);
				// TODO client.write();
			} else {
				Integer gameId=
			}
			while (true) {
				Message message = in.readMessage();
				gameRoom.sendAll(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
