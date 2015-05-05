package com.felix.game.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.felix.game.RTutil.UnitMoveModel;
import com.felix.game.dto.UnitPathDTO;
import com.felix.game.map.model.Location;
import com.felix.game.model.Game;
import com.felix.game.model.UserGame;
import com.felix.game.server.message.Message;
import com.felix.game.server.message.MessageType;
import com.felix.game.util.PasswordUtil;

public class GameRoom {
	private final Logger log = Logger.getLogger(getClass());
	private Game game;
	private List<Client> clients = new ArrayList<Client>();
	private Map<Integer, UnitMoveModel> unitMoveModels = new HashMap<Integer, UnitMoveModel>();

	public GameRoom() {
		game = new Game();
	}

	public GameRoom(Game game) {
		this.game = new Game();
		this.game.setGameId(game.getGameId());
		this.game.setMap(game.getMap());
		this.game.setGamePassword(PasswordUtil.instance().encode(
				game.getGamePassword()));

	}

	public void sendAll(Message message) {
		clients.forEach(c -> c.write(message));
	}

	/**
	 * 
	 * @param message
	 * @param fromId
	 *            this client will not receive the message
	 */
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
		int userId = client.getUser().getId();
		// find in game object
		for (UserGame ug : game.getUserGames())
			if (ug.getUser().getId() == userId) {
				client.setLocation(new Location(ug.getLocationX(), ug
						.getLocationY()));
				break;
			}
		if (client.getLocation() == null) {
			// TODO шось адекватне)
			client.setLocation(new Location(47, 74));
		}
		clients.forEach(c -> client.write(new Message(MessageType.UNIT_ADD, c
				.getUserLocationDTO())));
		clients.add(client);
		sendAll(new Message(MessageType.UNIT_ADD, client.getUserLocationDTO()));
		this.unitMoveModels.put(userId,
				new UnitMoveModel(this, userId, client.getLocation()));// (unitPath,
																		// speed);
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

	public void moveUnit(UnitPathDTO path) {
		sendAll(new Message(MessageType.UNIT_MOVE, path), path.getUserId());
		// TODO speed setting
		log.trace("unit move message sent");
	//	this.unitMoveModels.get(path.getUserId()).addMove(path, 1);
	}

	public void reportCrash(int userId, Location rejectedLocation,
			Location crashLocation) {
		List<Location> list = null;
		System.out.println(rejectedLocation);
		System.out.println(crashLocation);
		if (rejectedLocation != null && crashLocation != null) {
			list = new ArrayList<Location>(2);
			list.add(new Location(rejectedLocation));
			list.add(new Location(crashLocation));
			System.out.println(list);
		}
		log.info("sending user " + userId + (list == null ? "no" : "")
				+ " crash report");
		sendAll(new Message(MessageType.UNIT_CRASH, new UnitPathDTO(userId,
				list)));
	}

	public Map<Integer, UnitMoveModel> getUnitMoveModels() {
		return unitMoveModels;
	}

	public void setUnitMoveModels(Map<Integer, UnitMoveModel> unitMoveModels) {
		this.unitMoveModels = unitMoveModels;
	}

}
