package com.felix.game.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.felix.game.db.dao.impl.GameDao;
import com.felix.game.exception.DaoException;
import com.felix.game.model.Game;
import com.felix.game.server.message.Message;

public class Server {
	private Map<Integer, GameRoom> games = new ConcurrentHashMap<>();
	private Logger log = Logger.getLogger(getClass());

	public void sendAll(Message message) {
		games.forEach((id, game) -> game.sendAll(message));
	}

	public GameRoom addGame(Game game) {
		try {
			GameDao.instance().create(game);
		} catch (DaoException e) {
			log.error("failed to store in db");
		}
		System.out.println(game);
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
			Properties properties = new Properties();
			InputStream input = null;

			try {
				input = new FileInputStream("config/connection.properties");
				properties.load(input);
			} catch (IOException e) {
				log.error("failed to load properties file");
				e.printStackTrace();
			}
			Integer port = Integer.parseInt(properties.getProperty("port"));
			serverSocket = new ServerSocket(port);
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
