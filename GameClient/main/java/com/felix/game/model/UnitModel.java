package com.felix.game.model;

import java.util.List;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.felix.game.dto.UserLocationDTO;
import com.felix.game.map.model.Location;

public class UnitModel {
	private static final int UNIT_SIZE = 5;
	private int userId;
	private Location targetLocation;
	private UnitMove movingTask;
	private Color color;
	private Location location;

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
		new Thread(movingTask).start();
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

	public int getUnitX() {
		return location.getX();
	}

	public int getUnitY() {
		return location.getY();
	}


	public void paintLocation(GraphicsContext gc) {
		int x = getUnitX();
		int y = getUnitY();
		Platform.runLater(() -> {
			gc.setFill(getColor());
			gc.fillOval(x, y, UNIT_SIZE, UNIT_SIZE);
		});
	}

	public void paintTarget(GraphicsContext gc) {
		int x = getTargetX();
		int y = getTargetY();
		gc.setFill(Color.BLACK);
		gc.fillOval(x, y, UNIT_SIZE, UNIT_SIZE);
	}

	public void clearLocation(GraphicsContext gc) {
		int x = getUnitX();
		int y = getUnitY();
		Platform.runLater(() -> gc.clearRect(x, y, UNIT_SIZE, UNIT_SIZE));
	}

	public void clearTarget(GraphicsContext gc) {
		int x = getTargetX();
		int y = getTargetY();
		Platform.runLater(() -> gc.clearRect(x, y, UNIT_SIZE, UNIT_SIZE));
	}

	private int getTargetX() {
		return getTargetLocation().getX();
	}

	private int getTargetY() {
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
		private static final int DELAY = 25;
		private int brushCoef;
		private boolean isStopped;

		public void stop() {
			isStopped = true;
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

			for (Location point : path) {
				if (isStopped)
					break;
				Location scaledPoint = point.zoomIn(brushCoef);
				while (!isStopped
						&& unit.getLocation().equals(scaledPoint) == false) {
					beforeTime = System.currentTimeMillis();
					boolean diag;
					if (scaledPoint.getX() != unit.getLocation().getX()
							&& scaledPoint.getY() != unit.getLocation().getY())
						diag = true;
					else
						diag = false;

					sleep = DELAY;
					if (diag)
						sleep *= 1.414;

					int newX = unit.getLocation().getX();
					if (scaledPoint.getX() < unit.getLocation().getX())
						newX--;
					else if (scaledPoint.getX() > unit.getLocation().getX())
						newX++;
					int newY = unit.getLocation().getY();
					if (scaledPoint.getY() < unit.getLocation().getY())
						newY--;
					else if (scaledPoint.getY() > unit.getLocation().getY())
						newY++;

					Location newLocation = new Location(newX, newY);
					unit.move(newLocation, gc);
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
