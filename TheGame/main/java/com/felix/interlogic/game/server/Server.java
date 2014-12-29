package com.felix.interlogic.game.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.felix.socket.Message;

public class Server {
	private List<GameRoom> games = new ArrayList<GameRoom>();
	public static final int PORT = 6666;

	public void sendAll(Message message) {
		games.forEach(g -> g.sendAll(message));
	}

	public void addGame(GameRoom gameRoom) {
		this.games.add(gameRoom);
	}

	public void start() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(PORT);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				Client client = new Client(clientSocket);
				
				this.clients.add(new DataOutputStream(outs));
				new ServerThread(this, client).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server().start();
	}
}
