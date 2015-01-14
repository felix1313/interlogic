package com.felix.game.server;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.felix.game.db.dao.impl.GameDao;
import com.felix.game.db.dao.impl.UserDao;
import com.felix.game.exception.DaoException;
import com.felix.game.exception.IncorrectPasswordException;
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
			sendSuccessReport();
		} catch (DaoException ex) {
			ex.printStackTrace();
			sendFailureReport(MessageType.INVALID_USERNAME);
		} catch (Exception ex) {
			ex.printStackTrace();
			sendFailureReport(MessageType.OPERATION_FAIL);
		}

	}

	private void loginUser(User user) {
		try {
			User userDB = UserDao.instance().readByLogin(user.getLogin());
			if (PasswordUtil.instance().passwordEquals(user.getPassword(),
					userDB.getPassword()) == false)
				throw new IncorrectPasswordException();
			this.user = user;
			sendSuccessReport();
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
			else {
				gameRoom.addClient(this, game.getGamePassword());
			}
		} catch (IncorrectPasswordException ex) {
			sendFailureReport(MessageType.INCORRECT_PASSWORD);
		} catch (Exception ex) {
			ex.printStackTrace();
			sendFailureReport(MessageType.OPERATION_FAIL);
		}
	}

	private void startGame(Game game) {
		try {
			gameRoom = server.addGame(game);
			gameRoom.addClient(this, game.getGamePassword());
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

	public void write(Message m) {
		this.outputStream.write(m);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		while (true) {
			Message message = inputStream.readMessage();
			switch (message.getMessageType()) {
			case USER_REGISTER:
				registerUser((User) message.getData());
				break;
			case LOGIN:
				loginUser((User) message.getData());
				break;
			case GAME_LOAD:
				loadGame((Game) message.getData());
				break;
			case GAME_START:
				startGame((Game) message.getData());
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
}
