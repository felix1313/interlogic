package com.felix.game.model;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import javafx.scene.paint.Color;

import com.felix.game.dto.UserLocationDTO;
import com.felix.game.map.model.Location;

public class UnitModel extends MapObject {
	private final Logger log = Logger.getLogger(getClass());
	public static final int UNIT_SIZE = 10;
	private int userId;
	private Location targetLocation;
	private UnitMove movingTask;
	private MapModel map;
	private Color color;
	private ExecutorService moveExecutor = Executors.newSingleThreadExecutor();
	private ExecutorService shootExecutor = Executors.newCachedThreadPool();

	public UnitModel() {
		this.radius = UNIT_SIZE * 0.5;
	}

	public UnitModel(UserLocationDTO userLocationDTO, MapModel mapModel) {
		this();
		color = UnitModel.getRandomColor();
		setId(userLocationDTO.getUserId());
		setTargetLocation(new Location(userLocationDTO.getLocationX(),
				userLocationDTO.getLocationY()));
		this.location = new Location(userLocationDTO.getLocationX(),
				userLocationDTO.getLocationY());
		setLocation(getLocation().zoomIn(mapModel.getBrushcoef()));
		this.map = mapModel;
	}

	public int getId() {
		return userId;
	}

	public void setId(int id) {
		userId = id;
	}

	public void startMovement(List<Location> path, long ping) {
		stopMoving();
		this.movingTask = new UnitMove(this, path, ping);
		moveExecutor.execute(movingTask);
	}

	public void shoot(Location target) {
		shootExecutor.execute(new Bullet(this, target, getMap()).shoot());
	}

	public void crash(Location rejectedLocation, Location stopLocation) {
		if (movingTask != null)
			movingTask.crash(rejectedLocation, stopLocation);
		log.trace("crash: stopLocation = " + stopLocation);
	}

	public void stopMoving() {
		if (movingTask != null)
			movingTask.stop();
	}

	public boolean needsToMove() {
		return !getLocation().equals(targetLocation);
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

	public double getUnitX() {
		return location.getX();
	}

	public double getUnitY() {
		return location.getY();
	}

	public double getTargetX() {
		return getTargetLocation().getX();
	}

	public double getTargetY() {
		return getTargetLocation().getY();
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void move(Location newLocation) {
		map.clearLocation(this);
		Location oldLocation = getLocation();
		setLocation(newLocation);
		List<MapObject> crashList = map.getIntersectList(this);
		for (MapObject mo : crashList) {
			this.crash(mo);
		}
		if (this.movingTask.isStopped)
			setLocation(oldLocation);
		map.markUnit(this);
	}

	public Location getTargetLocation() {
		return targetLocation;
	}

	public void setTargetLocation(Location targetLocation) {
		this.targetLocation = targetLocation;
	}

	public class UnitMove implements Runnable {

		private List<Location> path;
		private UnitModel unit;
		private static final int DELAY = 100;
		private int brushCoef;
		private boolean crashes = false;
		private boolean isStopped = false;
		private Location rejectedLocation;
		private Location stopLocation;
		private long ping;

		public void crash(Location rejectedLocation, Location stopLocation) {
			this.rejectedLocation = rejectedLocation;
			this.stopLocation = stopLocation;
			this.crashes = true;
		}

		public void rejectCrash() {
			this.crashes = false;
		}

		public void stop() {
			this.isStopped = true;
		}

		public UnitMove(UnitModel unit, List<Location> path, long ping) {
			this.path = path;
			this.unit = unit;
			this.brushCoef = unit.map.getBrushcoef();
			this.ping = ping;
		}

		// TODO fix unused code
		@Override
		public void run() {
			log.trace("run");
			long beforeTime, timeDiff, sleep;
			for (int i = 1; i < path.size() && !isStopped; i++) {
				Location point = path.get(i);
				Location scaledPoint = point.zoomIn(brushCoef);
				long timePerCell = DELAY;
				boolean diag;
				if (scaledPoint.getX() != unit.getLocation().getX()
						&& scaledPoint.getY() != unit.getLocation().getY())
					diag = true;
				else
					diag = false;

				if (diag)
					timePerCell *= Math.sqrt(2.0);
				long timeOnMap = timePerCell / brushCoef;
				boolean onCrashSegment = false;
				while (!isStopped
						&& unit.getLocation().equals(scaledPoint) == false) {
					beforeTime = System.currentTimeMillis();
					// if (crashes && point.equals(rejectedLocation)) {
					// point = stopLocation;
					// onCrashSegment=true;
					// }else {
					point = path.get(i);
					// }
					scaledPoint = point.zoomIn(brushCoef);
					double newX = unit.getLocation().getX();
					if (scaledPoint.getX() < unit.getLocation().getX())
						newX = Math.max(newX - 1, scaledPoint.getX());
					else if (scaledPoint.getX() > unit.getLocation().getX())
						newX = Math.min(newX + 1, scaledPoint.getX());
					double newY = unit.getLocation().getY();
					if (scaledPoint.getY() < unit.getLocation().getY())
						newY = Math.max(newY - 1, scaledPoint.getY());
					else if (scaledPoint.getY() > unit.getLocation().getY())
						newY = Math.min(newY + 1, scaledPoint.getY());

					Location newLocation = new Location(newX, newY);
					unit.move(newLocation);
					sleep = timeOnMap;

					if (onCrashSegment && newLocation.equals(scaledPoint))
						isStopped = true;
					timeDiff = System.currentTimeMillis() - beforeTime;
					sleep -= timeDiff;
					if (sleep < 0) {
						sleep = 2;
					}
					long dif = Math.min(sleep - 2, ping);
					ping -= dif;
					sleep -= dif;
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}

	}

	@Override
	public String toString() {
		return "UnitModel [userId=" + userId + ", targetLocation="
				+ targetLocation + ", color=" + color + ", location="
				+ location + "]";
	}

	public MapModel getMap() {
		return map;
	}

	public void setMap(MapModel map) {
		this.map = map;
	}

	@Override
	public void crash(MapObject mo) {

		if (mo instanceof UnitModel) {
			log.info("crash report");
			this.stopMoving();
		} else {
			log.info("bullet crush");
			mo.crash(this);
		}
	}

	@Override
	public void paint() {
		map.markUnit(this);
	}
}
