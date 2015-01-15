package com.felix.game.model;

import javafx.scene.paint.Color;

import com.felix.game.dto.UserLocationDTO;

public class UnitModel {
	private UserLocationDTO userLocationDTO;
	private Color color;
	private int unitX, unitY;

	public UnitModel() {
	}

	public int getId() {
		return userLocationDTO.getUserId();
	}

	public UnitModel(UserLocationDTO userLocationDTO) {
		color = UnitModel.getRandomColor();
		this.userLocationDTO = userLocationDTO;
		this.unitX = getTargetX();
		this.unitY = getTargetY();
	}

	public boolean needsToMove() {
		return getTargetX() != getUnitX() || getTargetY() != getUnitY();
	}

	private static Color getRandomColor() {
		return new Color(Math.random(), Math.random(), Math.random(), 1);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getUnitX() {
		return unitX;
	}

	public void setUnitX(int unitX) {
		this.unitX = unitX;
	}

	public int getUnitY() {
		return unitY;
	}

	public void setUnitY(int unitY) {
		this.unitY = unitY;
	}

	public int getTargetY() {
		return userLocationDTO.getLocationY();
	}

	public void setTargetY(int unitY) {
		this.userLocationDTO.setLocationY(unitY);
	}

	public int getTargetX() {
		return userLocationDTO.getLocationX();
	}

	public void setTargetX(int unitX) {
		this.userLocationDTO.setLocationX(unitX);
	}

	public UserLocationDTO getUserLocationDTO() {
		return userLocationDTO;
	}

	public void setUserLocationDTO(UserLocationDTO userLocationDTO) {
		this.userLocationDTO = userLocationDTO;
	}

	@Override
	public String toString() {
		return "UnitModel [getUnitX()=" + getUnitX() + ", getUnitY()="
				+ getUnitY() + ", getTargetY()=" + getTargetY()
				+ ", getTargetX()=" + getTargetX() + "]";
	}
	

}
