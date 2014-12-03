package com.felix.interlogic.game.model;

import java.util.HashSet;
import java.util.Set;

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
	public Set<UserGame> userGames = new HashSet<UserGame>();

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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.game")
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

}
