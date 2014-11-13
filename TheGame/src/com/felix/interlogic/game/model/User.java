package com.felix.interlogic.game.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user")
public class User {
	private Integer id;
	private String login;
	private String password;
	private Game game;

	public User() {
	}

	public User(String login, String password) {
		this.login = login;
		this.password = password;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "login", unique = true, nullable = false)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Game.class, cascade=CascadeType.REFRESH)
	@JoinColumn(name = "game_id")
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password
				+ "]";
	}
}
