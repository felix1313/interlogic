package com.felix.game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.felix.game.db.dao.impl.GameDao;
import com.felix.game.model.Game;
import com.felix.game.server.message.Message;

public class Server {
	private Map<Integer, GameRoom> games = new ConcurrentHashMap<>();
	public static final int PORT = 6667;
	private Logger log = Logger.getLogger(getClass());

	public void sendAll(Message message) {
		games.forEach((id, game) -> game.sendAll(message));
	}

	public GameRoom addGame(Game game) {
		if (this.games.containsKey(game.getGameId())) {
			log.error("duplicate key");
			return null;
		}
		this.games.put(game.getGameId(), new GameRoom(game));
		return this.games.get(game.getGameId());
	}

	public GameRoom getGameRoom(Integer id) {
		if (!games.containsKey(id)) {
			Game game = GameDao.instance().read(id, true);
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
				log.info("starting new client thread " + client.getId());
				client.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server().start();
	}

	public Map<Integer, GameRoom> getGames() {
		return games;
	}
}
