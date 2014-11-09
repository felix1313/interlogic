package com.felix.interlogic.chat;

import java.awt.EventQueue;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.felix.interlogic.chat.swing.ChatWindow;
import com.felix.interlogic.socket.ClientThread;

public class ChatClient {
	private static final String IP = "127.0.0.1";
	private static final int PORT = 6666;
	private ChatWindow window;
	private DataOutputStream output;
	private Socket socket;

	public ChatClient() throws IOException {
		InetAddress ip = InetAddress.getByName(IP);
		socket = new Socket(ip, PORT);
		output = new DataOutputStream(socket.getOutputStream());
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				window = new ChatWindow(output);
				// window.setVisible(true);
				new ClientThread(ChatClient.this.socket, window).start();
			}
		});
		

	}

	public static void main(String[] args) {
		try {
			new ChatClient();
		} catch (IOException e) {
			System.out.println("failed to connect to server");
			e.printStackTrace();
		}
	}
}
