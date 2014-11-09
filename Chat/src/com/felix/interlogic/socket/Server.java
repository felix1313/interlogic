package com.felix.interlogic.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	private List<DataOutputStream> clients;
	public static final int PORT = 6666;

	public Server() {
		this.clients = new ArrayList<DataOutputStream>();
	}

	public void sendAll(String str) {
		for (DataOutputStream client : clients) {
			try {
				client.writeUTF(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(PORT);
			while (true) {
				Socket client = serverSocket.accept();
				OutputStream outs = client.getOutputStream();
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
