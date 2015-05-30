package com.felix.game.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import com.felix.game.map.model.Location;

public class UnitMover implements EventHandler<ActionEvent> {
	private ArrayList<Location> path;
	private UnitModel unit;
	private static final int DELAY = 15;
	private static final int PING_DELAY = 10;
	private int index = 0;
	private boolean isStopped = false;
	private Logger log = Logger.getLogger(getClass());
	private Timeline quickAnimation, animation;
	private SequentialTransition sequence;

	@Override
	public void handle(ActionEvent event) {
		if (path == null)
			return;
		if (index < path.size() && unit.getLocation().equals(path.get(index)))
			index++;
		if (index >= path.size()){
			stop();
			return;
		}
		Location point = path.get(index);

		Location newLocation;
		if (unit.getLocation().dist(point) < 1)
			newLocation = point;
		else {
			Location direction = unit.getLocation().vectorTo(point);
			direction = direction.zoomOut(direction.dist(new Location(0, 0)));
			newLocation = unit.getLocation().add(direction);
		}
		unit.move(newLocation);
	}

	public UnitMover(UnitModel unit) {
		this.unit = unit;
		quickAnimation = new Timeline();
		animation = new Timeline();
		animation.getKeyFrames().add(new KeyFrame(new Duration(DELAY), this));
		animation.setCycleCount(Timeline.INDEFINITE);
	}

	public void move(ArrayList<Location> path, long ping) {
		stop();
		isStopped = false;
		int quickCyclesCnt = (int) (ping / PING_DELAY);
		quickAnimation.getKeyFrames().clear();
		this.path = path;
		index = 0;
		// quickAnimation.getKeyFrames().add(
		// new KeyFrame(new Duration(PING_DELAY), this));
		quickAnimation.setCycleCount(quickCyclesCnt);

		// stop();

		sequence = new SequentialTransition(quickAnimation, animation);
		sequence.play();
	}

	public void stop() {
		isStopped = true;
		if (sequence != null)
			sequence.stop();
	}

	public boolean isStopped() {
		return isStopped;
	}
}
