package com.felix.interlogic.game.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class UserGameId implements Serializable {
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name = "user_id", updatable = false, insertable = false, referencedColumnName = "user_id")
	private User user;
	@ManyToOne
	@JoinColumn(name = "game_id", updatable = false, insertable = false, referencedColumnName = "game_id")
	private Game game;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}

}
