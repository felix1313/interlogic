package com.felix.game.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.felix.game.db.dao.impl.GameDao;
import com.felix.game.db.dao.impl.UserDao;
import com.felix.game.exception.DaoException;
import com.felix.game.exception.IncorrectPasswordException;
import com.felix.game.model.Game;
import com.felix.game.model.User;
import com.felix.game.server.message.Message;
import com.felix.game.server.message.MessageType;
import com.felix.game.util.PasswordUtil;

public class Server {
	private Map<Long, GameRoom> games = new ConcurrentHashMap<Long, GameRoom>();
	public static final int PORT = 6666;

	public void sendAll(Message message) {
		games.forEach((id, game) -> game.sendAll(message));
	}

	public GameRoom addGame(Game game) {
		return this.games.put(game.getGameId(), new GameRoom(game));
	}

	public GameRoom getGameRoom(Long id) {
		if (games.containsKey(id)) {
			Game game = GameDao.instance().read(id);
			if (game != null)
				games.put(id, new GameRoom(game));
		}
		return games.get(id);
	}

	public void start() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(PORT);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				Client client = new Client(clientSocket, this);
				client.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server().start();
	}

	public Map<Long, GameRoom> getGames() {
		return games;
	}
}
