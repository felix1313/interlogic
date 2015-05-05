package com.felix.game.model;

import java.util.List;

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
	private BulletShooter shootThread;

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

	public BulletShooter shoot() {
		return this.shootThread = new BulletShooter(this);
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

	public class BulletShooter implements Runnable {

		private static final long BULLET_SPEED = 10;
		private Bullet bullet;
		private MapModel map;
		private boolean stopped = false;

		public BulletShooter(Bullet bullet) {
			this.bullet = bullet;
			this.map = bullet.map;
		}

		public void stop() {
			this.stopped = true;
		}

		@Override
		public void run() {
			long beforeTime, timeDiff, sleep;
			while (!stopped) {
				beforeTime = System.currentTimeMillis();
				Location newLocation = bullet.getLocation().add(direction);
				if (!map.canGo(newLocation)) {
					bullet.crash(null);
					break;
				}
				Location oldLocation = bullet.getLocation();
				bullet.setLocation(newLocation);
				List<MapObject> crashList = map.getIntersectList(bullet);
				if (crashList.size() == 0) {
					Platform.runLater(() -> {
						bullet.setLocation(oldLocation);
						bullet.clear();
						bullet.setLocation(newLocation);
						bullet.paint();
					});
				} else {
					bullet.setLocation(oldLocation);
					for (MapObject mo : crashList) {
						mo.crash(bullet);
					}
					bullet.crash(null);
					break;
				}
				timeDiff = System.currentTimeMillis() - beforeTime;
				sleep = BULLET_SPEED - timeDiff;
				if (sleep < 2)
					sleep = 2;
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public Location getDirection() {
		return direction;
	}

	public void setDirection(Location direction) {
		this.direction = direction;
	}

	@Override
	public void crash(MapObject mo) {
		this.shootThread.stop();
		Platform.runLater(() -> {
			this.clear();
		});
	}

	public MapModel getMap() {
		return map;
	}

	public void setMap(MapModel map) {
		this.map = map;
	}

}
