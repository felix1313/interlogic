package com.felix.interlogic.game.model;

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

@Entity
@Table(name = "game")
public class Game {
	private Long gameId;
	private String gamePassword;
	public Set<User> players = new HashSet<>();

	public Game() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "game_id", unique = true, nullable = false)
	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	@Column(name = "password")
	public String getGamePassword() {
		return gamePassword;
	}

	public void setGamePassword(String gamePassword) {
		this.gamePassword = gamePassword;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "game", cascade = CascadeType.REFRESH)
	public Set<User> getPlayers() {
		return players;
	}

	public void setPlayers(Set<User> players) {
		this.players = players;
	}

	public void addPlayer(User u) {
		this.players.add(u);
	}

	@Override
	public String toString() {
		return "Game [gameId=" + gameId + ", gamePassword=" + gamePassword
				+ ", players=" + players + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameId == null) ? 0 : gameId.hashCode());
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
		return true;
	}

}
