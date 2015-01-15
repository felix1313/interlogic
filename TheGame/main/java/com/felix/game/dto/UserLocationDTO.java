package com.felix.game.dto;

import java.io.Serializable;

import com.felix.game.model.UserGame;

public class UserLocationDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private int userId;
	private int locationX;
	private int locationY;

	public UserLocationDTO() {

	}

	public UserLocationDTO(UserGame userGame) {
		this.userId = userGame.getUser().getId();
		this.locationX = userGame.getLocationX();
		this.locationY = userGame.getLocationY();
	}

	public UserLocationDTO(int userId, int locationX, int locationY) {
		this.userId = userId;
		this.locationX = locationX;
		this.locationY = locationY;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getLocationX() {
		return locationX;
	}

	public void setLocationX(int locationX) {
		this.locationX = locationX;
	}

	public int getLocationY() {
		return locationY;
	}

	public void setLocationY(int locationY) {
		this.locationY = locationY;
	}

	@Override
	public String toString() {
		return "UserLocationDTO [userId=" + userId + ", locationX=" + locationX
				+ ", locationY=" + locationY + "]";
	}

}
