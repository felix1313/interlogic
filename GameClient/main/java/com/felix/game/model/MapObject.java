package com.felix.game.model;

import com.felix.game.map.model.Location;

/**
 * Circle on map. represents Bullet or Unit
 * 
 * @author felix
 *
 */
public abstract class MapObject {
	protected Location location;
	protected double radius;

	public MapObject() {

	}

	public abstract void crash(MapObject mo);

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Location getCenter() {
		return new Location(location.getX() + radius, location.getY() + radius);
	}

	public boolean intersects(MapObject otherObject) {
		if (otherObject == null)
			return false;
		if (otherObject == this)
			return false;
		return circleIntersect(otherObject);
	}

	private boolean circleIntersect(MapObject otherObject) {
		//TODO magic constant fix
		return this.getCenter().dist(otherObject.getCenter()) - 1e-2 <= this
				.getRadius() + otherObject.getRadius();
	}

	private boolean squareIntersect(MapObject otherObject) {
		if (this.getLocation().getX() + this.radius * 2 < otherObject
				.getLocation().getX()
				|| this.getLocation().getX() > otherObject.getLocation().getX()
						+ otherObject.radius * 2)
			return false;
		if (this.getLocation().getY() + this.radius * 2 < otherObject
				.getLocation().getY()
				|| this.getLocation().getY() > otherObject.getLocation().getY()
						+ otherObject.radius * 2)
			return false;
		return true;
	}
	
	public abstract void paint();
	

}
