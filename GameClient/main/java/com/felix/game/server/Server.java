package com.felix.game.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

import javafx.application.Platform;

import org.apache.log4j.Logger;

import com.felix.game.dto.UnitPathDTO;
import com.felix.game.dto.UserLocationDTO;
import com.felix.game.map.model.Location;
import com.felix.game.model.ChatMessage;
import com.felix.game.model.Game;
import com.felix.game.model.User;
import com.felix.game.server.message.Message;
import com.felix.game.server.message.MessageInputStream;
import com.felix.game.server.message.MessageOutputStream;
import com.felix.game.server.message.MessageType;
import com.felix.game.view.WindowController;

public class Server extends Thread {
	private Socket socket;
	private static Server server;
	private final Logger log = Logger.getLogger(getClass());
	private MessageInputStream inputStream;
	private MessageOutputStream outputStream;
	private WindowController controller;

	private Server() {
		try {

			Properties properties = new Properties();
			InputStream input = null;

			try {
				log.trace("loading connection properties");
				input = (getClass()
						.getResourceAsStream("/config/connection.properties"));
				properties.load(input);
			} catch (Exception e) {
				log.error("failed to load connection properties "
						+ e.getMessage());
				e.printStackTrace();
				return;
			}
			String ipStr = properties.getProperty("ip");
			Integer port = Integer.parseInt(properties.getProperty("port"));
			InetAddress ip = InetAddress.getByName(ipStr);
			socket = new Socket(ip, port);
			inputStream = new MessageInputStream(socket.getInputStream());
			outputStream = new MessageOutputStream(socket.getOutputStream());
			log.info("connection established");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("failed to connect to server");
		}
	}

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

	public Message joinGame(Integer gameId, String pass) {
		outputStream.write(new Message(MessageType.GAME_LOAD, new Game(gameId,
				pass)));
		Message resp = inputStream.readMessage();
		return resp;
	}

	@SuppressWarnings("unchecked")
	public List<Game> getGameList() {
		log.info("retrieving game list..");
		outputStream.write(new Message(MessageType.GET_ACTIVE_GAMES));
		Message resp = inputStream.readMessage();
		if (resp.getMessageType() == MessageType.OPERATION_SUCCESS) {
			log.info("game list received");
			return (List<Game>) resp.getData();
		} else {
			log.warn("failed to receive list of games");
			return null;
		}
	}

	public Message createGame(Game game) {
		outputStream.write(new Message(MessageType.GAME_START, game));
		log.info("created game id:" + game.getGameId());
		Message resp = inputStream.readMessage();
		return resp;
	}

	public void moveUnit(UnitPathDTO targetLocation) {
		log.info("ask server to move unit to " + targetLocation);
		outputStream.write(new Message(MessageType.UNIT_MOVE, targetLocation));
		try {
			outputStream.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void shoot(Location target) {
		outputStream.write(new Message(MessageType.SHOOT_MESSAGE, target));
	}

	public void sendMessage(ChatMessage message) {
		log.info("ask server to send chat message");
		outputStream.write(new Message(MessageType.CHAT_MESSAGE, message));
	}

	@Override
	public void run() {
		while (true) {
			Message message = inputStream.readMessage();
			switch (message.getMessageType()) {
			case UNIT_MOVE:
				controller.move((UnitPathDTO) message.getData());
				log.info("move message received");
				break;
			case UNIT_STOP:
				log.info("stop message received");
				controller.stopMovement((int) message.getData());
				break;
			case UNIT_ADD:
				controller.addUnit((UserLocationDTO) message.getData());
				break;
			case SHOOT_MESSAGE:
				controller.shoot((UserLocationDTO) message.getData());
				break;
			case UNIT_CRASH:
				log.info("unit crash report received");
				// first element in unitPathDTO - rejected location ; second -
				// crash location
				controller.handleCrashMessage((UnitPathDTO) message.getData());
				break;
			case CHAT_MESSAGE:
				Platform.runLater(() -> {
					controller.chat((ChatMessage) message.getData());
				});
				log.info("chat message received");
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
