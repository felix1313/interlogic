package com.felix.game.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.felix.game.dto.UserLocationDTO;
import com.felix.game.model.Game;
import com.felix.game.model.User;
import com.felix.game.server.message.Message;
import com.felix.game.server.message.MessageInputStream;
import com.felix.game.server.message.MessageOutputStream;
import com.felix.game.server.message.MessageType;
import com.felix.game.view.WindowController;

public class Server extends Thread {
	private final static String IP = "127.0.0.1";
	private final static int PORT = 6667;
	private Socket socket;
	private static Server server;
	private final Logger log = Logger.getLogger(getClass());
	private MessageInputStream inputStream;
	private MessageOutputStream outputStream;
	private WindowController controller;

	private Server() {
		try {
			InetAddress ip = InetAddress.getByName(IP);
			socket = new Socket(ip, PORT);
			inputStream = new MessageInputStream(socket.getInputStream());
			outputStream = new MessageOutputStream(socket.getOutputStream());
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

	public Message login(String login, String pass) {
		outputStream
				.write(new Message(MessageType.LOGIN, new User(login, pass)));
		Message resp = inputStream.readMessage();
		return resp;
	}

	public Message register(String login, String pass) {
		outputStream.write(new Message(MessageType.USER_REGISTER, new User(
				login, pass)));
		Message resp = inputStream.readMessage();
		return resp;
	}

	public MessageType joinGame(Integer gameId, String pass) {
		outputStream.write(new Message(MessageType.GAME_LOAD, new Game(gameId,
				pass)));
		Message resp = inputStream.readMessage();
		return resp.getMessageType();
	}

	public MessageType createGame(String pass) {
		Game game = new Game(pass);
		outputStream.write(new Message(MessageType.GAME_START, game));
		log.info("created game id:" + game.getGameId());
		Message resp = inputStream.readMessage();
		return resp.getMessageType();
	}

	public void moveUnit(UserLocationDTO targetLocation) {
		log.info("ask server to move unit to " + targetLocation);
		outputStream.write(new Message(MessageType.UNIT_MOVE, targetLocation));
		try {
			outputStream.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			Message message = inputStream.readMessage();
			switch (message.getMessageType()) {
			case UNIT_MOVE:
				controller.move((UserLocationDTO) message.getData());
				log.info("move message recieved");
				break;
			case UNIT_ADD:
				controller.addUnit((UserLocationDTO) message.getData());
				break;
			default:
				log.error("unknown command " + message.getMessageType());
				break;
			}
		}

	}

	public WindowController getController() {
		return controller;
	}

	public void setController(WindowController controller) {
		this.controller = controller;
	}

}
