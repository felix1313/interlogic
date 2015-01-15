package com.felix.game.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_game", catalog = "the_game")
public class UserGame implements Serializable {

	// @EmbeddedId
	// UserGameId userGameId;

	private static final long serialVersionUID = 2343610173032407177L;

	@Id
	@ManyToOne
	@JoinColumn(name = "user_id", updatable = false, insertable = false, referencedColumnName = "user_id")
	// @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;

	@Id
	@ManyToOne
	@JoinColumn(name = "game_id", updatable = false, insertable = false, referencedColumnName = "game_id")
	private Game game;

	@ManyToOne
	@JoinColumn(name = "unit_id", referencedColumnName = "unit_id")
	private Unit unit;
	@Column(name = "level")
	private Integer level;
	@Column(name = "location_x")
	private Integer locationX;
	@Column(name = "location_y")
	private Integer locationY;

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getLocationX() {
		return locationX;
	}

	public void setLocationX(Integer locationX) {
		this.locationX = locationX;
	}

	public Integer getLocationY() {
		return locationY;
	}

	public void setLocationY(Integer locationY) {
		this.locationY = locationY;
	}

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
