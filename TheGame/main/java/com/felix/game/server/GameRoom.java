package com.felix.game.server;

import java.util.ArrayList;
import java.util.List;

import com.felix.game.dto.UserLocationDTO;
import com.felix.game.exception.IncorrectPasswordException;
import com.felix.game.model.Game;
import com.felix.game.model.UserGame;
import com.felix.game.server.message.Message;
import com.felix.game.server.message.MessageType;
import com.felix.game.util.PasswordUtil;

public class GameRoom {
	private Game game;
	private List<Client> clients = new ArrayList<Client>();

	public GameRoom() {
		game = new Game();
	}

	public GameRoom(Game game) {
		this.game = new Game();
		this.game.setGameId(game.getGameId());
		this.game.setGamePassword(PasswordUtil.instance().encode(
				game.getGamePassword()));
	}

	public void sendAll(Message message) {
		clients.forEach(c -> c.write(message));
	}

	public void sendAll(Message message, int fromId) {
		for (Client c : clients) {
			if (c.getUser().getId() != fromId)
				c.write(message);
		}
	}

	public boolean passwordOK(String password) {
		return PasswordUtil.instance().passwordEquals(password,
				game.getGamePassword());
	}

	public void addClient(Client client) {

		// find in game object
		for (UserGame ug : game.getUserGames())
			if (ug.getUser().getId() == client.getUser().getId()) {
				client.setUserLocationDTO(new UserLocationDTO(ug));
				break;
			}
		if (client.getUserLocationDTO() == null) {
			// TODO шось адекватне)
			client.setUserLocationDTO(new UserLocationDTO(client.getUser()
					.getId(), 47, 74));
		}
		clients.forEach(c -> client.write(new Message(MessageType.UNIT_ADD, c
				.getUserLocationDTO())));
		clients.add(client);
		sendAll(new Message(MessageType.UNIT_ADD, client.getUserLocationDTO()));
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
