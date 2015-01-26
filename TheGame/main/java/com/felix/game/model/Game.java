package com.felix.game.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.felix.game.map.model.Map;

@Entity
@Table(name = "game")
public class Game implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6417574237777065306L;
	private Integer gameId;
	private String gamePassword;
	transient private String mapFilePath;
	transient private Set<UserGame> userGames = new HashSet<UserGame>();
	private Map map;

	public Game() {

	}

	public Game(Game game) {
		this.gameId = game.gameId;
		this.gamePassword = game.gamePassword;
		this.map = game.map;
		this.mapFilePath = game.mapFilePath;
		this.userGames = game.userGames;
	}

	public Game(String password) {
		this.gamePassword = password;
	}

	public Game(int id, String password) {
		this.gameId = id;
		this.gamePassword = password;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "game_id", unique = true, nullable = false)
	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	@Column(name = "password")
	public String getGamePassword() {
		return gamePassword;
	}

	public void setGamePassword(String gamePassword) {
		this.gamePassword = gamePassword;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "game", cascade = CascadeType.ALL)
	public Set<UserGame> getUserGames() {

		return userGames;
	}

	public void setUserGame(Set<UserGame> userGames) {
		this.userGames = userGames;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
		result = prime * result
				+ ((gamePassword == null) ? 0 : gamePassword.hashCode());
		result = prime * result
				+ ((userGames == null) ? 0 : userGames.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (gameId == null) {
			if (other.gameId != null)
				return false;
		} else if (!gameId.equals(other.gameId))
			return false;
		if (gamePassword == null) {
			if (other.gamePassword != null)
				return false;
		} else if (!gamePassword.equals(other.gamePassword))
			return false;
		if (userGames == null) {
			if (other.userGames != null)
				return false;
		} else if (!userGames.equals(other.userGames))
			return false;
		return true;
	}

	public void setUserGames(Set<UserGame> userGames) {
		this.userGames = userGames;
	}

	@Column(name = "map_filepath")
	public String getMapFilePath() {
		return mapFilePath;
	}

	public void setMapFilePath(String mapFilePath) {
		this.mapFilePath = mapFilePath;
	}

	public void loadMap() throws FileNotFoundException, IOException,
			ClassNotFoundException {
		ObjectInputStream inp = new ObjectInputStream(new FileInputStream(
				mapFilePath));
		this.map = (Map) inp.readObject();
		inp.close();
	}

	@Transient
	public Map getMap() {
		if (map == null)
			try {
				loadMap();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	@Override
	public String toString() {
		return "Game [gameId=" + gameId + ", gamePassword=" + gamePassword
				+ ", map=" + map + "]";
	}

}
