package com.felix.game.dto;

import java.io.Serializable;

import com.felix.game.model.UserGame;

public class UserLocationDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private int userId;
	private double locationX;
	private double locationY;

	public UserLocationDTO() {

	}

	public UserLocationDTO(UserGame userGame) {
		this.userId = userGame.getUser().getId();
		this.locationX = userGame.getLocationX();
		this.locationY = userGame.getLocationY();
	}

	public UserLocationDTO(int userId, double locationX, double locationY) {
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

	public double getLocationX() {
		return locationX;
	}

	public void setLocationX(double locationX) {
		this.locationX = locationX;
	}

	public double getLocationY() {
		return locationY;
	}

	public void setLocationY(double locationY) {
		this.locationY = locationY;
	}

	@Override
	public String toString() {
		return "UserLocationDTO [userId=" + userId + ", locationX=" + locationX
				+ ", locationY=" + locationY + "]";
	}

}
