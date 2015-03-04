package com.felix.game.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.felix.game.db.dao.impl.UserDao;
import com.felix.game.dto.UnitPathDTO;
import com.felix.game.dto.UserLocationDTO;
import com.felix.game.exception.DaoException;
import com.felix.game.exception.IncorrectPasswordException;
import com.felix.game.map.model.Location;
import com.felix.game.model.Game;
import com.felix.game.model.User;
import com.felix.game.server.message.Message;
import com.felix.game.server.message.MessageInputStream;
import com.felix.game.server.message.MessageOutputStream;
import com.felix.game.server.message.MessageType;
import com.felix.game.util.PasswordUtil;

public class Client extends Thread {
	private final Logger log = Logger.getLogger(getClass());
	private Socket socket;
	private User user;
	private MessageOutputStream outputStream;
	private MessageInputStream inputStream;
	private Server server;
	private GameRoom gameRoom;
	private Location userLocation;

	public Client(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
		try {
			this.outputStream = new MessageOutputStream(
					socket.getOutputStream());
			this.inputStream = new MessageInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void registerUser(User user) {
		try {
			user.setPassword(PasswordUtil.instance().encode(user.getPassword()));
			UserDao.instance().create(user);
			this.user = user;
			sendUserId(user.getId());
		} catch (DaoException ex) {
			ex.printStackTrace();
			sendFailureReport(MessageType.INVALID_USERNAME);
		} catch (Exception ex) {
			ex.printStackTrace();
			sendFailureReport(MessageType.OPERATION_FAIL);
		}

	}

	private void sendUserId(int id) {
		write(new Message(MessageType.OPERATION_SUCCESS, id));
	}

	private void loginUser(User user) {
		try {
			User userDB = UserDao.instance().readByLogin(user.getLogin());
			if (PasswordUtil.instance().passwordEquals(user.getPassword(),
					userDB.getPassword()) == false)
				throw new IncorrectPasswordException();
			user.setId(userDB.getId());
			this.user = user;
			sendUserId(user.getId());
		} catch (DaoException e) {
			sendFailureReport(MessageType.INVALID_USERNAME);
			e.printStackTrace();
		} catch (IncorrectPasswordException e) {
			e.printStackTrace();
			sendFailureReport(MessageType.INCORRECT_PASSWORD);
		} catch (Exception ex) {
			ex.printStackTrace();
			sendFailureReport(MessageType.OPERATION_FAIL);
		}
	}

	private void loadGame(Game game) {
		try {
			gameRoom = server.getGameRoom(game.getGameId());
			if (gameRoom == null)
				sendFailureReport(MessageType.INVALID_GAMEID);
			else if (!gameRoom.passwordOK(game.getGamePassword())) {
				sendFailureReport(MessageType.INCORRECT_PASSWORD);
			} else {
				sendSuccessReport(gameRoom.getGame());

				gameRoom.addClient(this);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			sendFailureReport(MessageType.OPERATION_FAIL);
		}
	}

	private void startGame(Game game) {
		try {
			gameRoom = server.addGame(game);
			System.out.println(gameRoom.getGame());
			sendSuccessReport(gameRoom.getGame());
			gameRoom.addClient(this);

		} catch (Exception ex) {
			ex.printStackTrace();
			sendFailureReport(MessageType.OPERATION_FAIL);
		}
	}

	public void exit() {
		try {
			gameRoom.removeClient(this);
			sendSuccessReport();
		} catch (Exception e) {
			e.printStackTrace();
			sendFailureReport(MessageType.OPERATION_FAIL);
		}
	}

	public void sendFailureReport(MessageType failureType) {
		this.outputStream.write(new Message(failureType));
	}

	public void sendSuccessReport() {
		this.outputStream.write(new Message(MessageType.OPERATION_SUCCESS));
	}

	public void sendSuccessReport(Serializable data) {
		this.outputStream
				.write(new Message(MessageType.OPERATION_SUCCESS, data));
	}

	public void write(Message m) {
		this.outputStream.write(m);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void moveUnit(UnitPathDTO path) {
		this.userLocation = path.getPath().get(path.getPath().size() - 1);
		log.info("target location : " + userLocation);
		gameRoom.sendAll(new Message(MessageType.UNIT_MOVE, path), getUser()
				.getId());
	}

	private void sendMessage(Message message) {
		log.info("chat message");
		this.gameRoom.sendAll(message, user.getId());
	}

	private void sendActiveGames() {
		ArrayList<Game> games = new ArrayList<Game>();
		server.getGames().values()
				.forEach(gameroom -> games.add(gameroom.getGame()));
		log.info("sending games list");
		sendSuccessReport(games);
	}

	@Override
	public void run() {
		while (true) {
			Message message = inputStream.readMessage();
			log.info("message received: " + message);
			switch (message.getMessageType()) {
			case UNIT_MOVE:
				moveUnit((UnitPathDTO) message.getData());
				break;
			case USER_REGISTER:
				registerUser((User) message.getData());
				break;
			case GET_ACTIVE_GAMES:
				sendActiveGames();
				break;
			case LOGIN:
				loginUser((User) message.getData());
				break;
			case GAME_LOAD:
				loadGame((Game) message.getData());
				break;
			case GAME_START:
				startGame((Game) message.getData());
				break;
			case CHAT_MESSAGE:
				sendMessage(message);
				break;
			case EXIT:
				this.exit();
				break;
			default:
				sendFailureReport(MessageType.OPERATION_FAIL);
				log.error("unknown command");
				break;
			}
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserLocationDTO getUserLocationDTO() {
		return new UserLocationDTO(this.user.getId(), userLocation.getX(),
				userLocation.getY());
	}

	public void setLocation(Location location) {
		this.userLocation = location;
	}

	public Location getLocation() {
		return userLocation;
	}
}
