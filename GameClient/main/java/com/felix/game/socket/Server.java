package com.felix.game.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class Server {
	private final static String IP = "127.0.0.1";
	private final static int PORT = 6666;
	private Socket socket;
	private static Server server;
	private final Logger log = Logger.getLogger(getClass());

	private Server() {
		try {
			InetAddress ip = InetAddress.getByName(IP);
			socket = new Socket(ip, PORT);
			log.info("connection established");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("failed to connect to server");
		}
	};

	public static Server instance() {
		if (server == null) {
			server = new Server();
		}
		return server;
	}

}
