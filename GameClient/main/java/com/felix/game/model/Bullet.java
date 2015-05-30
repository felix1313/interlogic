package com.felix.game.model;


import com.felix.game.map.model.Location;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet extends MapObject {
	private static final int DEFAULT_SIZE = 4;
	private UnitModel shooter;
	private Location start;
	private Location direction;
	private MapModel map;
	public boolean isStopped = false;

	public Bullet(UnitModel shooter, Location target, MapModel map) {
		this.radius = DEFAULT_SIZE * 0.5;
		this.shooter = shooter;
		this.map = map;
		this.direction = shooter.getCenter().vectorTo(target);
		double len = direction.dist(new Location(0, 0));
		direction = direction.zoomIn((shooter.getRadius() + this.radius + 1)
				/ len);
		start = new Location(shooter.getCenter().add(direction));
		start.setX(start.getX() - radius);
		start.setY(start.getY() - radius);
		direction = direction.zoomOut(shooter.getRadius());
		this.location = new Location(start);
		Platform.runLater(() -> {
			paint();
		});
	}

	
	public void paint() {
		GraphicsContext gc = map.getUnitGraphics();
		gc.setFill(Color.RED);
		gc.fillOval(location.getX(), location.getY(), radius, radius);
		map.setOccupied(this);
	}

	public void clear() {
		GraphicsContext gc = map.getUnitGraphics();
		gc.clearRect(location.getX(), location.getY(), radius, radius);
		map.clearOccupied(this);
		map.repaintNeighbours(location, radius * 3);
	}

	public UnitModel getShooter() {
		return shooter;
	}

	public void setShooter(UnitModel shooter) {
		this.shooter = shooter;
	}

	public Location getStart() {
		return start;
	}

	public void setStart(Location start) {
		this.start = start;
	}

	public Location getDirection() {
		return direction;
	}

	public void setDirection(Location direction) {
		this.direction = direction;
	}

	@Override
	public void crash(MapObject mo) {
		this.isStopped = true;
		Platform.runLater(() -> {
			this.clear();
		});
	}

	public boolean isStopped() {
		return isStopped;
	}

	public MapModel getMap() {
		return map;
	}

	public void setMap(MapModel map) {
		this.map = map;
	}

}
