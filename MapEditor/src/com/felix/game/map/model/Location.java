package com.felix.game.map.model;

import java.io.Serializable;

public class Location implements Serializable {

	private static final long serialVersionUID = 6287897665472043709L;

	private int x;
	private int y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Location(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "Location [x=" + x + ", y=" + y + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Location other = (Location) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public Location zoomOut(int coef) {
		return new Location(x / coef, y / coef);
	}

	public Location zoomIn(int coef) {
		return new Location(x * coef, y * coef);
	}

	public double dist(Location other) {
		return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y)
				* (y - other.y));
	}

}
