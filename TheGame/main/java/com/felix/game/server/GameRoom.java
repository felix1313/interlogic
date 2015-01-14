package com.felix.game.server;

import java.util.ArrayList;
import java.util.List;

import com.felix.game.exception.IncorrectPasswordException;
import com.felix.game.model.Game;
import com.felix.game.server.message.Message;
import com.felix.game.util.PasswordUtil;

public class GameRoom {
	private Game game;
	private List<Client> clients = new ArrayList<Client>();

	public GameRoom() {
		game = new Game();
	}

	public GameRoom(Game game) {
		this.game = game;
	}

	public void sendAll(Message message) {
		clients.forEach(c -> c.write(message));
	}

	public boolean passwordOK(String password) {
		return PasswordUtil.instance().passwordEquals(password,
				game.getGamePassword());
	}

	public void addClient(Client client, String password)
			throws IncorrectPasswordException {
		if (passwordOK(password))
			clients.add(client);
		else
			throw new IncorrectPasswordException();
	}

	public void removeClient(Client client) {
		clients.remove(client);
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}
