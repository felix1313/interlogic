package com.felix.game.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import com.felix.game.map.model.Location;

public class BulletMover {

	private HashSet<Bullet> bullets;
	private static final long BULLET_SPEED = 10;

	private final Timeline animation = new Timeline();

	public BulletMover() {
		bullets = new HashSet<Bullet>();
		animation.getKeyFrames().add(
				new KeyFrame(new Duration(BULLET_SPEED),
						new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								Iterator<Bullet> iterator = bullets.iterator();
								Bullet bullet;

								while (true) {
									synchronized (bullets) {
										if (!iterator.hasNext())
											break;
										bullet = iterator.next();
									}
									if (!bullet.isStopped()) {
										MapModel map = bullet.getMap();
										Location newLocation = bullet
												.getLocation().add(
														bullet.getDirection());
										if (!map.canGo(newLocation)) {
											bullet.crash(null);
											break;
										}
										Location oldLocation = bullet
												.getLocation();
										bullet.setLocation(newLocation);
										List<MapObject> crashList = map
												.getIntersectList(bullet);
										if (crashList.size() == 0) {
											bullet.setLocation(oldLocation);
											bullet.clear();
											bullet.setLocation(newLocation);
											bullet.paint();
										} else {
											bullet.setLocation(oldLocation);
											for (MapObject mo : crashList) {
												mo.crash(bullet);
											}
											bullet.crash(null);
											break;
										}

									} else {
										synchronized (bullets) {
											iterator.remove();
										}
									}
								}
							}
						}));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
	}

	public void addBullet(Bullet bullet) {
		synchronized (bullets) {
			bullets.add(bullet);
		}
	}

}
