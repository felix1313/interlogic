package com.felix.game.model;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.felix.game.dto.UserLocationDTO;
import com.felix.game.map.model.Location;

public class UnitModel {
	private final Logger log = Logger.getLogger(getClass());
	private static final int UNIT_SIZE = 5;
	private int userId;
	private Location targetLocation;
	private UnitMove movingTask;
	private Color color;
	private Location location;
	private ExecutorService moveExecutor = Executors.newSingleThreadExecutor();

	public UnitModel() {
	}

	public int getId() {
		return userId;
	}

	public void setId(int id) {
		userId = id;
	}

	public void startMovement(List<Location> path, GraphicsContext gc, int brush) {
		stopMoving();
		this.movingTask = new UnitMove(this, path, gc, brush);
		moveExecutor.execute(movingTask);
	}

	public void rejectCrash(){
		
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

	public UnitModel(UserLocationDTO userLocationDTO) {
		color = UnitModel.getRandomColor();
		setId(userLocationDTO.getUserId());
		setTargetLocation(new Location(userLocationDTO.getLocationX(),
				userLocationDTO.getLocationY()));
		this.location = new Location(userLocationDTO.getLocationX(),
				userLocationDTO.getLocationY());
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

	public void paintLocation(GraphicsContext gc) {
		double x = getUnitX();
		double y = getUnitY();
		Platform.runLater(() -> {
			gc.setFill(getColor());
			gc.fillOval(x, y, UNIT_SIZE, UNIT_SIZE);
		});
	}

	public void paintTarget(GraphicsContext gc) {
		double x = getTargetX();
		double y = getTargetY();
		gc.setFill(Color.BLACK);
		gc.fillOval(x, y, UNIT_SIZE, UNIT_SIZE);
	}

	public void clearLocation(GraphicsContext gc) {
		double x = getUnitX();
		double y = getUnitY();
		Platform.runLater(() -> gc.clearRect(x, y, UNIT_SIZE, UNIT_SIZE));
	}

	public void clearTarget(GraphicsContext gc) {
		double x = getTargetX();
		double y = getTargetY();
		Platform.runLater(() -> gc.clearRect(x, y, UNIT_SIZE, UNIT_SIZE));
	}

	private double getTargetX() {
		return getTargetLocation().getX();
	}

	private double getTargetY() {
		return getTargetLocation().getY();
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void move(Location newLocation, GraphicsContext gc) {
		clearLocation(gc);
		setLocation(newLocation);
		paintLocation(gc);
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
		private GraphicsContext gc;
		private static final int DELAY = 100;
		private int brushCoef;
		private boolean crashes = false;
		private boolean isStopped = false;
		private Location rejectedLocation;
		private Location stopLocation;

		public void crash(Location rejectedLocation, Location stopLocation) {
			this.rejectedLocation = rejectedLocation;
			this.stopLocation = stopLocation;
			this.crashes = true;
		}
		public void rejectCrash(){
			this.crashes=false;
		}

		public void stop() {
			this.isStopped = true;
		}

		public UnitMove(UnitModel unit, List<Location> path,
				GraphicsContext gc, int brushCoef) {
			this.path = path;
			this.unit = unit;
			this.gc = gc;
			this.brushCoef = brushCoef;
		}

		@Override
		public void run() {
			long beforeTime, timeDiff, sleep;
			for (int i = 0; i < path.size() && !isStopped; i++) {
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
				
				while (!isStopped
						&& unit.getLocation().equals(scaledPoint) == false) {
					beforeTime = System.currentTimeMillis();
					if (crashes && point.equals(rejectedLocation)) {
						point = stopLocation;
					}else {
						point=path.get(i);
					}
					scaledPoint = point.zoomIn(brushCoef);
					double newX = unit.getLocation().getX();
					if (scaledPoint.getX() < unit.getLocation().getX())
						newX=Math.max(newX-1, scaledPoint.getX());
					else if (scaledPoint.getX() > unit.getLocation().getX())
						newX=Math.min(newX+1, scaledPoint.getX());
					double newY = unit.getLocation().getY();
					if (scaledPoint.getY() < unit.getLocation().getY())
						newY=Math.max(newY-1, scaledPoint.getY());
					else if (scaledPoint.getY() > unit.getLocation().getY())
						newY=Math.min(newY+1, scaledPoint.getY());

					Location newLocation = new Location(newX, newY);
					unit.move(newLocation, gc);
					sleep = timeOnMap;

					if(crashes && newLocation.equals(scaledPoint))isStopped=true;
					timeDiff = System.currentTimeMillis() - beforeTime;
					sleep -= timeDiff;

					if (sleep < 0)
						sleep = 2;
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
}
